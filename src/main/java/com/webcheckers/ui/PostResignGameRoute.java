package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameBoard;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.logging.Logger;

/**
 * @author Alec Jackson
 * @author Nicholas Curl
 */

public class PostResignGameRoute implements Route {

    private static final Logger LOG = Logger.getLogger(PostResignGameRoute.class.getName());
    private GameCenter gameCenter;
    private Gson gson;

    public PostResignGameRoute(GameCenter gameCenter, Gson gson){
        LOG.config("PostResignGameRoute is initialized.");
        this.gameCenter = gameCenter;
        this.gson = gson;
    }

    @Override
    public Object handle(Request request, Response response) {


        LOG.finer("PostResignGameRoute is invoked.");
        final Session httpSession = request.session();

        Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);
        GameBoard board = gameCenter.getGame(request.queryParams(GetGameRoute.GAMEID_PARAM));
        String json;
        if(board.hasResigned()&&player.isPlaying()){
            return gson.toJson(Message.error("Unable to resign, please submit a turn."));
        }

        board.resign(player);
        if(board.hasResigned()&&!(player.isPlaying())) {
            json = gson.toJson(Message.info("true"));
        }
        else{
            json = gson.toJson(Message.error("Unable to resign"));
        }
        return json;
    }
}