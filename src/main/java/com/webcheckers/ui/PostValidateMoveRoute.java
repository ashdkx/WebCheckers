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
 * @author Nicholas Curl
 */
public class PostValidateMoveRoute implements Route {

    private static final Logger LOG = Logger.getLogger(PostValidateMoveRoute.class.getName());

    private GameCenter gameCenter;

    public PostValidateMoveRoute(){
        LOG.config("PostValidateMoveRoute is initialized.");
    }

    @Override
    public Object handle(Request request, Response response){


        LOG.finer("PostValidateMoveRoute is invoked.");
        Gson gson = new Gson();
        final Session httpSession = request.session();


        Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);

        GameBoard board = player.getGame();
        List<Row> playerBoard;
        if(player.isPlayer1()){
            playerBoard = player.getGame().getPlayer1Board();
        }
        else{
            playerBoard = player.getGame().getPlayer2Board();
        }
        String json = request.queryParams("actionData");
        Move move = gson.fromJson(json,Move.class);
        boolean valid = false;
        String message = "";
        Integer[] pieceRemovePosition = new Integer[2];

        int moveStartRow = move.getStart().getRow();
        int moveStartCell = move.getStart().getCell();
        int moveEndRow = move.getEnd().getRow();
        int moveEndCell = move.getEnd().getCell();

        if (board.getActivePiece() == null){
            board.setActivePiece(board.getPiece(playerBoard,moveStartRow,moveStartCell));
            board.setActivePieceStart(move.getStart());
        }

        if(board.getActivePiece().getType() == Piece.type.SINGLE){
            if(moveEndRow>moveStartRow){
                message = "Can't move backwards.";
            }
            else if(board.isValid(playerBoard,moveEndRow,moveEndCell)){
                switch (moveEndCell-moveStartCell){
                    case -1:
                    case 1:
                        if(player.isSingleMove()){
                            valid = false;
                            message = "Can only move diagonally once.";
                        }
                        else if(board.getActivePieceMoves()>1){
                            valid = false;
                            message = "Cannot move after jump.";
                        }
                        else{
                            valid = true;
                            message = "Valid move.";
                            board.setActivePieceEnd(move.getEnd());
                            board.incrementActivePieceMoves();
                            player.setSingleMove(true);
                        }
                        break;
                    case -2:
                        if(!player.isSingleMove()) {
                            if (board.getPiece(playerBoard, moveStartRow - 1, moveEndCell + 1) != null) {
                                Piece piece = board.getPiece(playerBoard, moveStartRow - 1, moveEndCell + 1);


                                switch (piece.getColor()) {
                                    case RED:
                                        if (player.getColor() != GameBoard.color.RED) {
                                            valid = true;
                                            message = "Valid move.";
                                            pieceRemovePosition[0] = moveStartRow - 1;
                                            pieceRemovePosition[1] = moveEndCell + 1;
                                            board.addPieceRemove(pieceRemovePosition);
                                            board.incrementActivePieceMoves();
                                            board.setActivePieceEnd(move.getEnd());
                                        } else {
                                            valid = false;
                                            message = "Can't jump over own piece.";
                                        }
                                        break;
                                    case WHITE:
                                        if (player.getColor() != GameBoard.color.WHITE) {
                                            valid = true;
                                            message = "Valid move.";
                                            pieceRemovePosition[0] = moveStartRow - 1;
                                            pieceRemovePosition[1] = moveEndCell + 1;
                                            board.addPieceRemove(pieceRemovePosition);
                                            board.incrementActivePieceMoves();
                                            board.setActivePieceEnd(move.getEnd());
                                        } else {
                                            valid = false;
                                            message = "Can't jump over own piece.";
                                        }
                                        break;
                                }
                            } else {
                                valid = false;
                                message = "No piece to Jump.";
                            }
                        }
                        else {
                            valid = false;
                            message = "Cannot jump after single move.";
                        }
                        break;
                    case 2:
                        if(!player.isSingleMove()) {
                            if (board.getPiece(playerBoard, moveStartRow - 1, moveEndCell - 1) != null) {
                                Piece piece = board.getPiece(playerBoard, moveStartRow - 1, moveEndCell - 1);
                                switch (piece.getColor()) {
                                    case RED:
                                        if (player.getColor() != GameBoard.color.RED) {
                                            valid = true;
                                            message = "Valid move.";
                                            pieceRemovePosition[0] = moveStartRow - 1;
                                            pieceRemovePosition[1] = moveEndCell - 1;
                                            board.addPieceRemove(pieceRemovePosition);
                                            board.incrementActivePieceMoves();
                                            board.setActivePieceEnd(move.getEnd());
                                        } else {
                                            valid = false;
                                            message = "Can't jump over own piece.";
                                        }
                                        break;
                                    case WHITE:
                                        if (player.getColor() != GameBoard.color.WHITE) {
                                            valid = true;
                                            message = "Valid move.";
                                            pieceRemovePosition[0] = moveStartRow - 1;
                                            pieceRemovePosition[1] = moveEndCell - 1;
                                            board.addPieceRemove(pieceRemovePosition);
                                            board.incrementActivePieceMoves();
                                            board.setActivePieceEnd(move.getEnd());
                                        } else {
                                            valid = false;
                                            message = "Can't jump over own piece.";
                                        }
                                        break;
                                }
                            } else {
                                valid = false;
                                message = "No piece to Jump.";
                            }
                        }
                        else{
                            valid = false;
                            message = "Cannot jump after single move.";
                        }
                        break;
                    default:
                        valid = false;
                        message = "Invalid move";
                        break;

                }
            }
            else{
                message = "Invalid Move.";
            }


        }
        if(valid){
            json = gson.toJson(Message.info(message));
        }
        else {
            json = gson.toJson(Message.error(message));
        }

        return json;
    }
}
