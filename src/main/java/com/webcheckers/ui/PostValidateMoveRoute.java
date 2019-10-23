package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameBoard;
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
        System.out.println("Playerboard: "+playerBoard);
        String json = request.queryParams("actionData");
        System.out.println(json);
        Move move = gson.fromJson(json,Move.class);
        boolean valid = false;
        String message = "";

        int moveStartRow = move.getStart().getRow();
        int moveStartCell = move.getStart().getCell();
        int moveEndRow = move.getEnd().getRow();
        int moveEndCell = move.getEnd().getCell();

        if (board.getActivePiece() == null){
            board.setActivePiece(board.getPiece(playerBoard,moveStartRow,moveStartCell));
            System.out.println(board.getActivePiece().getColor());
            board.setActivePieceStart(move.getStart());
        }

        if(board.getActivePiece().getType() == Piece.type.SINGLE){
            if(moveEndRow>moveStartRow){
                valid = false;
                message = "Can't move backwards.";
            }
            else if(board.isValid(playerBoard,moveEndRow,moveEndCell)){
                switch (moveEndCell-moveStartCell){
                    case -1:
                    case 1:
                        board.incrementActivePieceMoves();
                        if(board.getActivePieceMoves()>1){
                            valid = false;
                            message = "Can only move diagonally once.";
                            board.decrementActivePieceMoves();
                        }
                        else{
                            valid = true;
                            message = "Valid move.";
                            board.setActivePieceEnd(move.getEnd());
                        }
                        break;
                    default:
                        valid = false;
                        message = "Can't move more than once space diagonally.";
                        break;

                }
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
