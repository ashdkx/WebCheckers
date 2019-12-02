package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameBoard;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.logging.Logger;

/**
 * The Ajax Controller for resigning a game.
 *
 * @author Alec Jackson
 * @author Nicholas Curl
 */

public class PostResignGameRoute implements Route {

    /**
     * The logger of this class
     */
    private static final Logger LOG = Logger.getLogger(PostResignGameRoute.class.getName());

    /**
     * The game center from the server
     */
    private GameCenter gameCenter;

    /**
     * The Gson instance from the server
     */
    private Gson gson;

    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /resignGame} HTTP Ajax requests.
     *
     * @param gameCenter The instance of the GameCenter
     * @param gson       The instance of Gson
     */
    public PostResignGameRoute(GameCenter gameCenter, Gson gson) {
        LOG.config("PostResignGameRoute is initialized.");
        this.gameCenter = gameCenter;
        this.gson = gson;
    }

    /**
     * Handle the WebCheckers Resign Ajax requests
     *
     * @param request  The HTTP request
     * @param response The HTTP response
     * @return The json of the message of whether if the player was able resign or not
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("PostResignGameRoute is invoked.");

        final Session httpSession = request.session();

        Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);
        GameBoard board = gameCenter.getGame(request.queryParams(GetGameRoute.GAMEID_PARAM));
        String json;
        if (board.hasResigned() && player.isPlaying()) { // check to see if a player resigned and it's not the current player
            return gson.toJson(Message.error("Unable to resign, please submit a turn."));
        }

        board.resign(player); // resign the current player
        if (board.hasResigned() && !(player.isPlaying())) { // check to see if it was successful
            json = gson.toJson(Message.info("true"));
        } else {
            json = gson.toJson(Message.error("Unable to resign"));
        }
        return json;
    }
}