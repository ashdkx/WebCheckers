package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameBoard;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.logging.Logger;

/**
 * The Ajax Controller for checking if it's a player's turn.
 *
 * @author Nicholas Curl
 */

public class PostCheckTurnRoute implements Route {

    /**
     * The logger of this class
     */
    private static final Logger LOG = Logger.getLogger(PostCheckTurnRoute.class.getName());

    /**
     * The game center from the server
     */
    private GameCenter gameCenter;

    /**
     * The Gson instance from the server
     */
    private Gson gson;


    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /checkTurn} HTTP Ajax requests.
     *
     * @param gameCenter The instance of the GameCenter
     * @param gson The instance of Gson
     */
    public PostCheckTurnRoute(GameCenter gameCenter, Gson gson){
        LOG.config("PostCheckTurnRoute is initialized.");
        this.gameCenter = gameCenter;
        this.gson = gson;
    }

    /**
     * Handle the WebCheckers CheckTurn Ajax requests
     *
     * @param request The HTTP request
     * @param response The HTTP response
     * @return The json of the message of whether the if it's player's turn or not, or if the game is over
     */
    @Override
    public Object handle(Request request, Response response) {


        LOG.finer("PostCheckTurnRoute is invoked.");
        final Session httpSession = request.session();

        Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);
        GameBoard board = gameCenter.getGame(request.queryParams(GetGameRoute.GAMEID_PARAM));

        String json;
        if (board.checkGameOver()){ //checks if game should be over
            json = gson.toJson(Message.info("true"));
        }
        else {
            if (board.isMyTurn(player)) { //checks to see if it's the current player's turn
                json = gson.toJson(Message.info("true"));
            } else {
                json = gson.toJson(Message.info("false"));
            }
        }
        return json;
    }
}
