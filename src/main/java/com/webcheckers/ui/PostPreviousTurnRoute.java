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
 * The Ajax Controller for going to the previous move in replay.
 *
 * @author Nicholas Curl
 */
public class PostPreviousTurnRoute implements Route {

    /**
     * The logger of this class
     */
    private static final Logger LOG = Logger.getLogger(PostPreviousTurnRoute.class.getName());

    /**
     * The game center from the server
     */
    private GameCenter gameCenter;

    /**
     * The Gson instance from the server
     */
    private Gson gson;

    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /replay/previousTurn} HTTP Ajax requests.
     *
     * @param gameCenter The instance of the GameCenter
     * @param gson       The instance of Gson
     */
    public PostPreviousTurnRoute(GameCenter gameCenter, Gson gson) {
        LOG.config("PostPreviousTurnRoute is initialized.");
        this.gameCenter = gameCenter;
        this.gson = gson;
    }

    /**
     * Handle the WebCheckers PreviousTurn Ajax requests
     *
     * @param request  The HTTP request
     * @param response The HTTP response
     * @return The json of the message of whether if it was able to go to the previous move or not
     */
    @Override
    public Object handle(Request request, Response response) {

        LOG.finer("PostPreviousTurnRoute is invoked.");

        SavedGame savedGame = gameCenter.getGameSave(request.queryParams(GetGameRoute.GAMEID_PARAM));// get the saved game
        int previousTurnNumber = savedGame.getTurnNumber(); // store the current turn number before going to the previous turn
        savedGame.previousTurn(); // go to the previous turn
        if (savedGame.getTurnNumber() < previousTurnNumber) {// checks to see if the turn number was successfully decremented
            if (savedGame.getTurnNumber() < 0) { // checks to see if the new turn number is outside the start
                savedGame.setTurnNumber(0); //reset back to the start of the moves
                return gson.toJson(Message.error("Could not advance to previous move."));
            } else {
                return gson.toJson(Message.info("true")); //if everything is fine return a json string of the message with true as the message and the type info
            }
        } else {
            return gson.toJson(Message.error("Could not advance to previous move."));
        }
    }
}
