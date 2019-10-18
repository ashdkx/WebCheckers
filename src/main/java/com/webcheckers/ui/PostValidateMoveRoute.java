package com.webcheckers.ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.webcheckers.appl.GameBoard;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Move;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class PostValidateMoveRoute implements Route {

    private static final Logger LOG = Logger.getLogger(PostValidateMoveRoute.class.getName());

    private final TemplateEngine templateEngine;
    private final GameCenter gameCenter;


    public PostValidateMoveRoute(GameCenter gameCenter, final TemplateEngine templateEngine){
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");

        this.gameCenter = gameCenter;

        LOG.config("PostValidateMoveRoute is initialized.");
    }

    @Override
    public Object handle(Request request, Response response){


        LOG.finer("PostValidateMoveRoute is invoked.");
        Gson gson = new Gson();
        final Session httpSession = request.session();


        Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);

        GameBoard board = player.getGame();

        String json = request.queryParams("actionData");
        System.out.println(json);
        Move move = gson.fromJson(json,Move.class);
        boolean valid = false;
        String message = "";
        if(board.getPiece(move.getStart().getRow(),move.getStart().getCell()).getType() == Piece.type.SINGLE){
            Piece piece1 = board.getPiece(move.getStart().getRow(),move.getStart().getCell());
            if(move.getEnd().getRow()>=move.getStart().getRow()){
                valid = false;
                message = "Can't move backwards.";
            }
            else if(board.isValid(move.getEnd().getRow(),move.getEnd().getCell())){
                switch (move.getEnd().getCell()-move.getStart().getCell()){
                    case -1:

                        valid = true;
                        message = "Valid move.";
                        break;
                    case 1:
                        valid = true;
                        message = "Valid move.";
                        break;
                    case -2:
                        if(board.getPiece(move.getStart().getRow()-1,move.getEnd().getCell()+1)!=null){
                            Piece piece = board.getPiece(move.getStart().getRow()-1,move.getEnd().getCell()+1);
                            switch (piece.getColor()){
                                case RED:
                                    valid = false;
                                    message = "Can't jump over own piece.";
                                    break;
                                case WHITE:
                                    valid = true;
                                    message = "Valid move.";
                                    break;
                            }
                        }
                        break;
                    case 2:
                        if(board.getPiece(move.getStart().getRow()-1,move.getEnd().getCell()-1)!=null){
                            Piece piece = board.getPiece(move.getStart().getRow()-1,move.getEnd().getCell()-1);
                            switch (piece.getColor()){
                                case RED:
                                    valid = false;
                                    message = "Can't jump over own piece.";
                                    break;
                                case WHITE:
                                    valid = true;
                                    message = "Valid move.";
                                    break;
                            }
                        }
                        break;
                    default:
                        valid = false;
                        message = "Can't move more than once space diagonally.";
                        break;

                }
            }

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
