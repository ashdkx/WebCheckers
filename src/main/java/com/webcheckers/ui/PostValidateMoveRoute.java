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
import java.util.Objects;
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
        String json = request.queryParams("actionData");
        System.out.println(json);
        Move move = gson.fromJson(json,Move.class);
        boolean valid = false;
        String message = "";
        if (board.getActivePiece() == null){
            board.setActivePiece(board.getPiece(move.getStart().getRow(),move.getStart().getCell()));
            board.setActivePieceStart(move.getStart());
        }

        if(board.getActivePiece().getType() == Piece.type.SINGLE){
            if(move.getEnd().getRow()>move.getStart().getRow()){
                valid = false;
                message = "Can't move backwards.";
            }
            else if(board.isValid(playerBoard,move.getEnd().getRow(),move.getEnd().getCell())){
                switch (move.getEnd().getCell()-move.getStart().getCell()){
                    case -1:
                    case 1:
                        valid = true;
                        message = "Valid move.";
                        break;
                    default:
                        valid = false;
                        message = "Can't move more than once space diagonally.";
                        break;

                }
            }
            board.setActivePieceEnd(move.getEnd());

        }
        String json2 = "";
        if(valid){
            json2 = gson.toJson(Message.info(message));
        }
        else {
            json2 = gson.toJson(Message.error(message));
        }

        return json2;
    }
}
