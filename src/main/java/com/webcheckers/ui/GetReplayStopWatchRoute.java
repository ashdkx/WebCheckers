package com.webcheckers.ui;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Player;
import com.webcheckers.model.SavedGame;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


/**
 * The UI Controller to GET the ReplayStopWatch page.
 *
 * @author Nicholas Curl
 */
public class GetReplayStopWatchRoute implements Route {

    /**
     * The logger for this class
     */
    private static final Logger LOG = Logger.getLogger(GetReplayStopWatchRoute.class.getName());

    /**
     * The value of this route's title
     */
    static final String TITLE = "Replay Exit";

    /**
     * The game center from the server
     */
    private final GameCenter gameCenter;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /replay/stopWatching} HTTP requests.
     *
     * @param gameCenter The instance of the GameCenter
     */
    public GetReplayStopWatchRoute(final GameCenter gameCenter) {
        this.gameCenter = gameCenter;
        //
        LOG.config("GetReplayStopWatchRoute is initialized.");
    }

    /**
     * Render the WebCheckers Replay stopWatch page.
     *
     * @param request  The HTTP request
     * @param response The HTTP response
     * @return The rendered HTML for the Replay stopWatch page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetReplayStopWatchRoute is invoked.");

        final Session httpSession = request.session();
        //
        Map<String, Object> vm = new HashMap<>();
        vm.put(GetHomeRoute.TITLE_ATTR, TITLE);

        final Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);
        final SavedGame savedGame = gameCenter.getGameSave(request.queryParams(GetGameRoute.GAMEID_PARAM)); // get the saved game
        savedGame.resetSavedGame(); //resets the saved game back to the default state
        player.setReplaying(false); // sets the current player to not watching a game replay
        vm.put(GetHomeRoute.CURRENT_USER_ATTR, player);
        response.redirect(WebServer.HOME_URL); // redirects home
        return null;
    }
}
