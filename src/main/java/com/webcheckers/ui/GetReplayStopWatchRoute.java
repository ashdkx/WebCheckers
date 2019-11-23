package com.webcheckers.ui;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Player;
import com.webcheckers.model.SavedGame;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Nicholas Curl
 */
public class GetReplayStopWatchRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetReplayStopWatchRoute.class.getName());

    static final String TITLE = "Replay Exit";
    private final GameCenter gameCenter;


    public GetReplayStopWatchRoute(final GameCenter gameCenter) {
        this.gameCenter = gameCenter;
        //
        LOG.config("GetReplayStopWatchRoute is initialized.");
    }

    @Override
    public Object handle(Request request, Response response) {
        final Session httpSession = request.session();
        LOG.finer("GetReplayStopWatchRoute is invoked.");
        //
        Map<String, Object> vm = new HashMap<>();
        vm.put(GetHomeRoute.TITLE_ATTR, TITLE);

        final Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);
        final SavedGame savedGame = gameCenter.getGameSave(request.queryParams(GetGameRoute.GAMEID_PARAM)); // get the saved game
        savedGame.resetSavedGame(); //resets the saved game back to the default state
        player.setReplaying(false); // sets the current player to not watching a game replay
        vm.put(GetHomeRoute.CURRENT_USER_ATTR,player);
        response.redirect(WebServer.HOME_URL); // redirects home
        return null;
    }
}
