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

import java.util.List;

import java.util.logging.Logger;

/**
 * @author Nicholas Curl
 */

public class PostSubmitTurnRoute implements Route {

    private static final Logger LOG = Logger.getLogger(PostSubmitTurnRoute.class.getName());
    private Gson gson = new Gson();



    public PostSubmitTurnRoute(){
        LOG.config("PostSubmitTurnRoute is initialized.");
    }

    @Override
    public Object handle(Request request, Response response) {


        LOG.finer("PostSubmitTurnRoute is invoked.");

        final Session httpSession = request.session();

        Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);
        Player player2;
        String json;
        Message message;
        GameBoard board = player.getGame();
        List<Row> playerBoard;
        if(player.isPlayer1()){
            playerBoard = board.getPlayer1Board();
            player2 = board.getPlayer2();
        }
        else{
            playerBoard = board.getPlayer2Board();
            player2 = board.getPlayer1();
        }

        Position moveEnd = board.getActiveEnd();
        Position moveStart = board.getActiveStart();



        if (player.isSingleMove()) {
            json = submit(board,playerBoard,player,moveStart, moveEnd);
        }
        else{
            if(canStillJump(board,player,playerBoard,moveEnd.getRow(),moveEnd.getCell())){
                json = gson.toJson(Message.error("Still able to jump."));
            }
            else {
                while (!board.getPieceRemove().isEmpty()) {
                    Integer[] position = board.removePieceRemove();
                    if (board.getPiece(playerBoard, position[0], position[1]).getType() == Piece.type.KING) {
                        player2.removeTotalPieces(2);
                    } else {
                        player2.removeTotalPieces(1);
                    }
                    playerBoard.get(position[0]).getSpace(position[1]).setPiece(null);
                }
                json = submit(board,playerBoard,player,moveStart, moveEnd);
            }
        }
        return json;
    }

    private boolean canStillJump(GameBoard board, Player player, List<Row> playerBoard, int moveEndRow, int moveEndCell) {
        boolean validPos1 = false;
        boolean validPos2 = false;
        if (board.getActivePiece().getType() == Piece.type.SINGLE) {
            if (board.getPiece(playerBoard, moveEndRow - 1, moveEndCell + 1) != null) {
                Piece piece = board.getPiece(playerBoard, moveEndRow - 1, moveEndCell + 1);
                boolean isCorrectColor = player.isActiveColor(piece);
                boolean isValidSpace = board.isValid(playerBoard,moveEndRow-2,moveEndCell+2);
                validPos1 = isValidSpace&&isCorrectColor;
            }
            if (board.getPiece(playerBoard, moveEndRow - 1, moveEndCell - 1) != null) {
                Piece piece = board.getPiece(playerBoard, moveEndRow - 1, moveEndCell - 1);
                boolean isCorrectColor = player.isActiveColor(piece);
                boolean isValidSpace = board.isValid(playerBoard,moveEndRow-2,moveEndCell-2);
                validPos2 = isCorrectColor&&isValidSpace;
            }
        }
        return validPos1&&validPos2;
    }

    private String submit(GameBoard board, List<Row> playerBoard, Player player, Position moveStart, Position moveEnd){
        board.setPiece(playerBoard, moveStart.getRow(), moveStart.getCell(), null);
        board.setPiece(playerBoard, moveEnd.getRow(), moveEnd.getCell(), board.getActivePiece());
        board.setActivePiece(null);
        board.setActivePieceMoves(0);

        if(player.isPlayer1()){
            board.updatePlayer2();
            player.setMyTurn(false);
            board.getPlayer2().setMyTurn(true);
        }
        else{
            board.updatePlayer1();
            player.setMyTurn(false);
            board.getPlayer1().setMyTurn(true);
        }
        player.setSingleMove(false);
        return gson.toJson(Message.info("Valid Move."));
    }

}
