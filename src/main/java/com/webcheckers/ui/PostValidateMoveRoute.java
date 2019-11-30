package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameBoard;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Move;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import com.webcheckers.model.Row;
import com.webcheckers.util.Message;
import spark.*;

import java.util.List;
import java.util.logging.Logger;


/**
 * The AJax Controller for validating a move.
 *
 * @author Nicholas Curl
 */
public class PostValidateMoveRoute implements Route {

    /**
     * The logger of this class
     */
    private static final Logger LOG = Logger.getLogger(PostValidateMoveRoute.class.getName());

    /**
     * The game center from the server
     */
    private GameCenter gameCenter;

    /**
     * The Gson instance from the server
     */
    private Gson gson;

    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /validateTurn} HTTP Ajax requests.
     *
     * @param gameCenter The instance of the GameCenter
     * @param gson       The instance of Gson
     */
    public PostValidateMoveRoute(GameCenter gameCenter, Gson gson) {
        LOG.config("PostValidateMoveRoute is initialized.");
        this.gameCenter = gameCenter;
        this.gson = gson;
    }

    /**
     * Handle the WebCheckers ValidateTurn Ajax requests
     *
     * @param request  The HTTP request
     * @param response The HTTP response
     * @return The json of the message of whether if the turn is valid or not
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("PostValidateMoveRoute is invoked.");

        final Session httpSession = request.session();
        Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);
        GameBoard board = gameCenter.getGame(request.queryParams(GetGameRoute.GAMEID_PARAM));

        List<Row> playerBoard = board.getPlayerBoard(player);

        String json = request.queryParams("actionData");
        Move move = gson.fromJson(json, Move.class);

        int moveStartRow = move.getStart().getRow();
        int moveStartCell = move.getStart().getCell();

        if (board.getActivePiece() == null) { //if the active piece is null, set the active piece
            board.setActivePiece(board.getPiece(playerBoard, moveStartRow, moveStartCell));
            board.setActivePieceStart(move.getStart());
        }
        return move(board, player, playerBoard, move);
    }

    /**
     * A helper function to check the move is either a jump or a single diagonal and whether it's valid
     *
     * @param board       The game board
     * @param player      The player moving the piece
     * @param playerBoard The board used for reference
     * @param move        The move
     * @return The json string of whether the move is valid or not
     */
    private String move(GameBoard board, Player player, List<Row> playerBoard, Move move) {
        int moveStartRow = move.getStart().getRow(); //the row of the starting position
        int moveStartCell = move.getStart().getCell(); // the cell of the starting position
        int moveEndRow = move.getEnd().getRow(); // the row of the end position
        int moveEndCell = move.getEnd().getCell(); // the cell in the end position
        String json;
        if (board.isValidSpace(playerBoard, moveEndRow, moveEndCell)) { //checks to see if the space being moved to is valid
            if (board.getActivePiece().getType() == Piece.type.SINGLE && moveEndRow > moveStartRow) { //if the piece is a single type and attempts to go backwards, output an error
                json = gson.toJson(Message.error("Can't move backwards."));
            } else {
                switch (moveEndCell - moveStartCell) { //check to see if a jump or a single move
                    case -1:// single move
                    case 1:
                        if (board.isSingleMove()) { //check to see if already moved
                            json = gson.toJson(Message.error("Can only move diagonally once."));
                        } else if (board.getActivePieceMoves() > 1) { //checks to see if trying to move after jump
                            json = gson.toJson(Message.error("Cannot move after jump."));
                        } else { //if everything is fine push the end position onto the activePieceEnd stack, increment the number of activePieceMoves and flag that a single move diagonally occurred
                            board.addActivePieceEnd(move.getEnd());
                            board.incrementActivePieceMoves();
                            board.setSingleMove(true);
                            json = gson.toJson(Message.info("Valid move."));
                        }
                        break;
                    case -2: //jump
                        if (board.getActivePiece().getType() == Piece.type.SINGLE) { //checks to see if the type is a single piece
                            json = jump(board, player, playerBoard, moveEndRow + 1, moveEndCell + 1, move);
                        } else { //king type
                            switch (moveEndRow - moveStartRow) { //checks to see which direction the king piece went
                                case 2: //move backwards
                                    json = jump(board, player, playerBoard, moveEndRow - 1, moveEndCell + 1, move);
                                    break;
                                case -2: //move forwards
                                    json = jump(board, player, playerBoard, moveEndRow + 1, moveEndCell + 1, move);
                                    break;
                                default: //neither
                                    json = gson.toJson(Message.error("Invalid move."));
                            }
                        }
                        break;
                    case 2:
                        if (board.getActivePiece().getType() == Piece.type.SINGLE) { //checks to see if the type is a single piece
                            json = jump(board, player, playerBoard, moveEndRow + 1, moveEndCell - 1, move);
                        } else { //king type
                            switch (moveEndRow - moveStartRow) { //checks to see which direction the king piece went
                                case 2: //move backwards
                                    json = jump(board, player, playerBoard, moveEndRow - 1, moveEndCell - 1, move);
                                    break;
                                case -2: // move forwards
                                    json = jump(board, player, playerBoard, moveEndRow + 1, moveEndCell - 1, move);
                                    break;
                                default: //neither
                                    json = gson.toJson(Message.error("Invalid move."));
                            }
                        }
                        break;
                    default: //if not a jump or single move
                        json = gson.toJson(Message.error("Invalid move."));

                }
            }
        } else { //not a valid space
            json = gson.toJson(Message.error("Invalid move."));
        }
        return json;
    }

    /**
     * A helper function to see if the jump is valid
     *
     * @param board          The game board
     * @param player         The player moving the piece
     * @param playerBoard    The board used for reference
     * @param pieceJumpedRow The row number of the piece being jumped
     * @param pieceJumpedCol The column number of the piece being jumped
     * @param move           The move
     * @return The json string of whether the jump is valid or not
     */
    private String jump(GameBoard board, Player player, List<Row> playerBoard, int pieceJumpedRow, int pieceJumpedCol, Move move) {
        int[] position = new int[2];
        if (!board.isSingleMove()) { //checks to see if the piece has already moved once diagonally
            if (board.getPiece(playerBoard, pieceJumpedRow, pieceJumpedCol) != null) { //checks to see if there is a piece to be jumped
                Piece piece = board.getPiece(playerBoard, pieceJumpedRow, pieceJumpedCol);

                if (board.isNotPlayerColor(piece, player)) {//checks to see if the piece being jumped is not the same color as the player
                    position[0] = pieceJumpedRow;
                    position[1] = pieceJumpedCol;
                    board.addPieceRemove(position);
                    board.incrementActivePieceMoves();
                    board.addActivePieceEnd(move.getEnd());
                    return gson.toJson(Message.info("Valid Move."));
                } else {
                    return gson.toJson(Message.error("Can't jump over own piece."));
                }
            } else {
                return gson.toJson(Message.error("No piece to Jump."));
            }
        } else {
            return gson.toJson(Message.error("Cannot jump after single move."));
        }
    }
}
