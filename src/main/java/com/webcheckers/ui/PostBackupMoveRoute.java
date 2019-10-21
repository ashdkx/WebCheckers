package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

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



        String json2 = "";
        player.getGame().setActivePiece(null);
        json2 = gson.toJson(Message.info("Backup Successful."));

        return json2;
    }


}
