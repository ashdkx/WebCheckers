package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.SavedGame;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.logging.Logger;

/**
 * @author Nicholas Curl
 */
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
        SavedGame savedGame = gameCenter.getGameSave(request.queryParams(GetGameRoute.GAMEID_PARAM)); // get the saved game
        int previousTurnNumber = savedGame.getTurnNumber(); // store the current turn number before going to the next turn
        savedGame.nextTurn(); // go to the next turn
        if(savedGame.getTurnNumber()>previousTurnNumber){ // checks to see if the turn number was successfully incremented
            if (savedGame.getTurnNumber() > savedGame.getSavedGameMoves().size()-1){ // checks to see if the new turn number is outside the total moves
                savedGame.setTurnNumber(savedGame.getSavedGameMoves().size()-1); //reset back to the end of the moves
                return gson.toJson(Message.error("Could not advance to next move."));
            }
            else {
                return gson.toJson(Message.info("true")); //if everything is fine return a json string of the message with true as the message and the type info
            }
        }
        else {
            return gson.toJson(Message.error("Could not advance to next move."));
        }
    }
}
