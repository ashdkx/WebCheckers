package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameBoard;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.logging.Logger;


/**
 * @author Nicholas Curl
 */
public class PostBackupMoveRoute implements Route {


    private static final Logger LOG = Logger.getLogger(PostBackupMoveRoute.class.getName());




    public PostBackupMoveRoute(){


        LOG.config("PostBackupMoveRoute is initialized.");
    }

    @Override
    public Object handle(Request request, Response response) {


        LOG.finer("PostBackupMoveRoute is invoked.");
        Gson gson = new Gson();
        final Session httpSession = request.session();
        Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);
        GameBoard board = player.getGame();
        board.decrementActivePieceMoves();
        if(board.getActivePieceMoves()<=0){
            board.resetActivePieceMoves();
            board.setActivePiece(null);
        }

        String json = "";
        player.getGame().setActivePiece(null);
        json = gson.toJson(Message.info("Backup Successful."));

        return json;
    }


}
