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
 * @author Nicholas Curl
 */

public class PostSubmitTurnRoute implements Route {

    private static final Logger LOG = Logger.getLogger(PostSubmitTurnRoute.class.getName());
    private Gson gson;
    private GameCenter gameCenter;

    public PostSubmitTurnRoute(GameCenter gameCenter, Gson gson){
        LOG.config("PostSubmitTurnRoute is initialized.");
        this.gameCenter = gameCenter;
        this.gson = gson;
    }

    @Override
    public Object handle(Request request, Response response) {


        LOG.finer("PostSubmitTurnRoute is invoked.");

        final Session httpSession = request.session();

        Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);
        String json;
        GameBoard board = gameCenter.getGame(request.queryParams(GetGameRoute.GAMEID_PARAM));
        List<Row> playerBoard = board.getPlayerBoard(player);

        Position moveEnd = board.getActivePieceEnd();
        Position moveStart = board.getActivePieceStart();
        int[] position = new int[]{moveStart.getRow(), moveStart.getCell()};

        checkRequiredMoves(board, player, playerBoard);
        if(board.getRequiredMovePieces().isEmpty()){ //if there are no required moves handle movement normally
            json = move(board,player, playerBoard,moveStart, moveEnd);
        }
        else {
            if(board.isRequiredMovePiece(position)&&hasJumped(board,position)) { //checks to see if the piece moved is a required move and has jumped
                json = move(board,player,playerBoard,moveStart,moveEnd);
            }
            else { // required move not fulfilled
                json = gson.toJson(Message.error("There are pieces that have to jump."));
                board.clearRequiredMovePieces();
            }
        }

        return json;
    }


    private boolean hasJumped(GameBoard board, int[] position){
        boolean jumped = false;
        for (int[] jumpPositions : board.getRequiredMoveJumps(position)){ //go through the required jumps for the piece at position
            for (int[] jumps : board.getPieceRemove()) { //go through the pieces that are to be removed
                if (jumps[0] == jumpPositions[0] && jumps[1] == jumpPositions[1]) { //checks to see if a piece to be removed is a required jump
                    jumped = true;
                    break;
                }
            }
        }
        return jumped;
    }

    private void checkRequiredMoves(GameBoard board, Player player, List<Row> playerBoard){
        for(int i = 7; i>-1;i--){ //go through the entire board
            for(int j = 0; j<8;j++){
                Piece piece = board.getPiece(playerBoard,i,j);
                if(board.isNotPlayerColor(piece,player)||piece==null) { //if the piece is not the players color or there is no piece continue on
                    continue;
                }
                if (board.canJump(player,playerBoard,i,j,piece)){ //check if the current piece can jump
                    int[] position = new int [] {i,j};
                    List<int[]> jumpPositionsAdd = new ArrayList<>(board.getJumpPositions());
                    board.addRequiredMovePieces(position,jumpPositionsAdd); //add piece and possible jumps to required moves
                    board.getJumpPositions().clear();
                }
            }
        }
    }

    private String submit(GameBoard board, List<Row> playerBoard, Player player, Position moveStart, Position moveEnd){
        board.setPiece(playerBoard, moveStart.getRow(), moveStart.getCell(), null); // remove old piece position

        if(moveEnd.getRow()==0){ //Check to see if piece reached the end of the board and crown the piece
            board.setPieceKing(player,playerBoard,moveEnd.getRow(),moveEnd.getCell());
            board.addPlayerTotalPieces(player);
        }
        else{
            board.setPiece(playerBoard, moveEnd.getRow(), moveEnd.getCell(), board.getActivePiece());
        }

        if(board.isRedPlayer(player)){
            board.updateWhitePlayer(); //update white player's board
            board.setPlayerTurn(board.getWhitePlayer()); //set to white player's turn

        }
        else{
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
        return gson.toJson(Message.info("Valid Move."));
    }


    private String move(GameBoard board, Player player, List<Row> playerBoard, Position moveStart, Position moveEnd){
        String json;
        if (board.isSingleMove()) { //check to see if piece moved one space diagonally
            json = submit(board, playerBoard, player, moveStart, moveEnd);
        } else {
            if (board.canJump(player, playerBoard, moveEnd.getRow(), moveEnd.getCell(), board.getActivePiece())) { //handles multiple jump
                json = gson.toJson(Message.error("Piece is still able to jump."));
            } else {
                while (!board.getPieceRemove().isEmpty()) { //goes through the entire list of pieces to remove
                    int[] positionRemove = board.removePieceRemove();
                    if (board.getPiece(playerBoard, positionRemove[0], positionRemove[1]).getType() == Piece.type.KING) {
                        board.removeOpponentTotalPieces(player,2); //remove two pieces from the opponent

                    } else {
                        board.removeOpponentTotalPieces(player,1); //remove one piece from the opponent
                    }
                    board.setPiece(playerBoard,positionRemove[0],positionRemove[1],null);  //remove the piece that was jumped
                }
                json = submit(board, playerBoard, player, moveStart, moveEnd);
            }
        }
        return json;
    }
}
