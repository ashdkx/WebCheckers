package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

public class PostCheckTurnRoute implements Route {

    private static final Logger LOG = Logger.getLogger(PostCheckTurnRoute.class.getName());


    public PostCheckTurnRoute(){


        LOG.config("PostCheckTurnRoute is initialized.");
    }

    @Override
    public Object handle(Request request, Response response) {


        LOG.finer("PostCheckTurnRoute is invoked.");
        Gson gson = new Gson();
        final Session httpSession = request.session();

        Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);

        String json= "";
        if (player.isMyTurn()){
            json = gson.toJson(Message.info("true"));
        }
        else {
            json = gson.toJson(Message.info("false"));
        }




        return json;
    }
}
