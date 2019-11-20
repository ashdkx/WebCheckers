package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameBoard;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.SavedGame;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.logging.Logger;

public class PostNextTurnRoute implements Route {

    private static final Logger LOG = Logger.getLogger(PostNextTurnRoute.class.getName());
    private GameCenter gameCenter;
    private Gson gson;
    public PostNextTurnRoute(GameCenter gameCenter, Gson gson){
        LOG.config("PostNextTurnRoute is initialized.");
        this.gameCenter = gameCenter;
        this.gson = gson;
    }

    @Override
    public Object handle(Request request, Response response) {

        LOG.finer("PostNextTurnRoute is invoked.");
        SavedGame savedGame = gameCenter.getGameSave(request.queryParams(GetGameRoute.GAMEID_PARAM));
        int previousTurnNumber = savedGame.getTurnNumber();
        savedGame.nextTurn();
        if(savedGame.getTurnNumber()>previousTurnNumber){
            if (savedGame.getTurnNumber() > savedGame.getSavedGameMoves().size()-1){
                savedGame.setTurnNumber(savedGame.getSavedGameMoves().size()-1);
                return gson.toJson(Message.error("Could not advance to next move."));
            }
            else {
                return gson.toJson(Message.info("true"));
            }
        }
        else {
            return gson.toJson(Message.error("Could not advance to next move."));
        }
    }
}
