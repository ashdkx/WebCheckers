package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.SavedGame;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.logging.Logger;

public class PostPreviousTurnRoute implements Route {

    private static final Logger LOG = Logger.getLogger(PostPreviousTurnRoute.class.getName());
    private GameCenter gameCenter;
    private Gson gson;
    public PostPreviousTurnRoute(GameCenter gameCenter, Gson gson){
        LOG.config("PostPreviousTurnRoute is initialized.");
        this.gameCenter = gameCenter;
        this.gson = gson;
    }

    @Override
    public Object handle(Request request, Response response) {

        LOG.finer("PostPreviousTurnRoute is invoked.");
        SavedGame savedGame = gameCenter.getGameSave(request.queryParams(GetGameRoute.GAMEID_PARAM));
        int previousTurnNumber = savedGame.getTurnNumber();
        savedGame.previousTurn();
        if(savedGame.getTurnNumber()<previousTurnNumber){
            if(savedGame.getTurnNumber() < 0){
                savedGame.setTurnNumber(0);
                return gson.toJson(Message.error("Could not advance to previous move."));
            }
            else {
                return gson.toJson(Message.info("true"));
            }
        }
        else {
            return gson.toJson(Message.error("Could not advance to previous move."));
        }
    }

}
