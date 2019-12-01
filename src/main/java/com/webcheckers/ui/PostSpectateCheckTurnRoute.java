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
 */

public class PostSpectateCheckTurnRoute implements Route {

    private static final Logger LOG = Logger.getLogger(PostCheckTurnRoute.class.getName());
    private GameCenter gameCenter;
    private Gson gson;

    public PostSpectateCheckTurnRoute(GameCenter gameCenter, Gson gson){
        LOG.config("PostSpectateSpectateCheckTurnRoute is initialized.");
        this.gameCenter = gameCenter;
        this.gson = gson;
    }

    @Override
    public Object handle(Request request, Response response) {


        LOG.finer("PostSpectateCheckTurnRoute is invoked.");
        final Session httpSession = request.session();

        Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);
        GameBoard board = gameCenter.getGame(request.queryParams(GetGameRoute.GAMEID_PARAM));

        String json;
        if (player.getSpectatorCount() != board.getTurnCount() || board.isGameOver()){
            player.setSpectatorCount(board.getTurnCount());
            json = gson.toJson(Message.info("true"));
        }
        else {
            json = gson.toJson(Message.info("false"));
            }
        return json;
    }
}
