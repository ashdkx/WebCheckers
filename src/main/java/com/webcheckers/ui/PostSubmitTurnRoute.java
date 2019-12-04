package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameBoard;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import com.webcheckers.model.Position;
import com.webcheckers.model.Row;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

import java.util.ArrayList;
import java.util.List;

import java.util.logging.Logger;


/**
 * The Ajax Controller for submitting a turn.
 *
 * @author Nicholas Curl
 */
public class PostSubmitTurnRoute implements Route {

    /**
     * The logger of this class
     */
    private static final Logger LOG = Logger.getLogger(PostSubmitTurnRoute.class.getName());

    /**
     * The Gson instance from the server
     */
    private Gson gson;

    /**
     * The game center from the server
     */
    private GameCenter gameCenter;

    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /submitTurn} HTTP Ajax requests.
     *
     * @param gameCenter The instance of the GameCenter
     * @param gson       The instance of Gson
     */
    public PostSubmitTurnRoute(GameCenter gameCenter, Gson gson) {
        LOG.config("PostSubmitTurnRoute is initialized.");
        this.gameCenter = gameCenter;
        this.gson = gson;
    }

    /**
     * Handle the WebCheckers SubmitTurn Ajax requests
     *
     * @param request  The HTTP request
     * @param response The HTTP response
     * @return The json of the message of whether if the player was able to submit a turn or not
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("PostSubmitTurnRoute is invoked.");

        final Session httpSession = request.session();

        Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);

        GameBoard board = gameCenter.getGame(request.queryParams(GetGameRoute.GAMEID_PARAM));
        List<Row> playerBoard = board.getPlayerBoard(player);

        String json;

        Position moveEnd = board.getActivePieceEnds().peek();
        Position moveStart = board.getActivePieceStart();
        int[] position = new int[]{moveStart.getRow(), moveStart.getCell()};

        checkRequiredMoves(board, player, playerBoard);
        if (board.getRequiredMovePieces().isEmpty()) { //if there are no required moves handle movement normally
            json = move(board, player, playerBoard, moveStart, moveEnd);
        } else {
            if (board.isRequiredMovePiece(position) && hasJumped(board, position)) { //checks to see if the piece moved is a required move and has jumped
                json = move(board, player, playerBoard, moveStart, moveEnd);
            } else { // required move not fulfilled
                json = gson.toJson(Message.error("There are pieces that have to jump."));
                board.clearRequiredMovePieces();
            }
        }

        return json;
    }

    /**
     * Helper function to see if a required move piece has jumped or not
     *
     * @param board    The game board
     * @param position The position of the piece being moved
     * @return True if the required piece has jumped a piece and false otherwise
     */
    private boolean hasJumped(GameBoard board, int[] position) {
        boolean jumped = false;
        for (int[] jumpPositions : board.getRequiredMoveJumps(position)) { //go through the required jumps for the piece at position
            for (int[] jumps : board.getPieceRemove()) { //go through the pieces that are to be removed
                if (jumps[0] == jumpPositions[0] && jumps[1] == jumpPositions[1]) { //checks to see if a piece to be removed is a required jump

                    jumped = true;
                    break;
                }
            }
        }

        return jumped;
    }

    /**
     * A helper function to check if the piece being moved is a required move piece
     *
     * @param board       The game board
     * @param player      The player moving the piece
     * @param playerBoard The board used for reference
     */
    private void checkRequiredMoves(GameBoard board, Player player, List<Row> playerBoard) {
        for (int i = 7; i > -1; i--) { //go through the entire board
            for (int j = 0; j < 8; j++) {
                Piece piece = board.getPiece(playerBoard, i, j);
                if (board.isNotPlayerColor(piece, player) || piece == null) { //if the piece is not the players color or there is no piece continue on
                    continue;
                }
                if (board.canJump(player, playerBoard, i, j, piece)) { //check if the current piece can jump
                    int[] position = new int[]{i, j};
                    List<int[]> jumpPositionsAdd = new ArrayList<>(board.getJumpPositions());
                    board.addRequiredMovePieces(position, jumpPositionsAdd); //add piece and possible jumps to required moves
                    board.getJumpPositions().clear();
                }
            }
        }
    }

    /**
     * Submits the turn by setting and clearing the required pieces
     *
     * @param board       The game board
     * @param playerBoard The board used as a reference
     * @param player      The player submitting the turn
     * @param moveStart   The starting move of the active piece
     * @param moveEnd     The ending position of the active piece
     * @return The json string of whether the move was submitted or not
     */
    private String submit(GameBoard board, List<Row> playerBoard, Player player, Position moveStart, Position moveEnd) {
        if (board.hasResigned()) { //check to see if the opponent has resigned and reset everything
            board.setActivePiece(null);
            board.setActivePieceMoves(0);
            board.setSingleMove(false);
            board.clearRequiredMovePieces();
            board.clearActivePieceEnd();
            board.getJumpPositions().clear();
            return gson.toJson(Message.info("Opponent Resigned"));
        } else {
            board.setPiece(playerBoard, moveStart.getRow(), moveStart.getCell(), null); // remove old piece position


            if (board.isActivePieceCrown()) { //Check to see if piece reached the end of the board and crown the piece
                board.setPieceKing(player, playerBoard, moveEnd.getRow(), moveEnd.getCell());
                board.addPlayerTotalPieces(player);
            } else {
                board.setPiece(playerBoard, moveEnd.getRow(), moveEnd.getCell(), board.getActivePiece());
            }

            if (board.isRedPlayer(player)) {
                board.updateWhitePlayer(); //update white player's board
                board.setPlayerTurn(board.getWhitePlayer()); //set to white player's turn

            } else {
                board.updateRedPlayer(); //update red player's board
                board.setPlayerTurn(board.getRedPlayer()); //set to red player's turn
            }

            //reset everything for the next turn
            board.setActivePiece(null);
            board.setActivePieceMoves(0);
            board.setSingleMove(false);
            board.clearRequiredMovePieces();
            board.clearActivePieceEnd();
            board.getJumpPositions().clear();
            board.setActivePieceCrown(false);
            board.checkGameOver();
            board.addMove(); // stores a move
            return gson.toJson(Message.info("Valid Move."));
        }
    }

    /**
     * A helper function to check the type of move of the active piece and compute accordingly
     *
     * @param board       The game board
     * @param player      The player moving the piece
     * @param playerBoard The board used as a reference
     * @param moveStart   The starting move of the active piece
     * @param moveEnd     The ending move of the active piece
     * @return The json on string of whether the turn was successfully submitted or not
     */
    private String move(GameBoard board, Player player, List<Row> playerBoard, Position moveStart, Position moveEnd) {
        String json;
        if (board.isSingleMove()) { //check to see if piece moved one space diagonally
            json = submit(board, playerBoard, player, moveStart, moveEnd);
        } else {
            if (board.canJump(player, playerBoard, moveEnd.getRow(), moveEnd.getCell(), board.getActivePiece())) { //handles multiple jump
                json = gson.toJson(Message.error("Active piece is still able to jump."));
            } else {
                while (!board.getPieceRemove().isEmpty()) { //goes through the entire list of pieces to remove
                    int[] positionRemove = board.removePieceRemove();
                    if (board.getPiece(playerBoard, positionRemove[0], positionRemove[1]).getType() == Piece.type.KING) {
                        board.removeOpponentTotalPieces(player, 2); //remove two pieces from the opponent

                    } else {
                        board.removeOpponentTotalPieces(player, 1); //remove one piece from the opponent
                    }
                    board.setPiece(playerBoard, positionRemove[0], positionRemove[1], null);  //remove the piece that was jumped
                }
                json = submit(board, playerBoard, player, moveStart, moveEnd);
            }
        }
        return json;
    }
}
