package com.webcheckers.ui;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Player;
import com.webcheckers.model.SavedGame;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Alec Jackson
 */
public class GetSpectateStopWatchingRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetReplayStopWatchRoute.class.getName());

    static final String TITLE = "Spectate Exit";
    private final GameCenter gameCenter;


    public GetSpectateStopWatchingRoute(final GameCenter gameCenter) {
        this.gameCenter = gameCenter;
        //
        LOG.config("GetSpectateStopWatch is initialized.");
    }

    @Override
    public Object handle(Request request, Response response) {
        final Session httpSession = request.session();
        LOG.finer("GetSpectateStopWatching is invoked.");
        //
        Map<String, Object> vm = new HashMap<>();
        vm.put(GetHomeRoute.TITLE_ATTR, TITLE);

        final Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);

        player.setSpectating(false); // sets the current player to not watching a game;
        player.setSpectatorCount(0);
        vm.put(GetHomeRoute.CURRENT_USER_ATTR,player);
        response.redirect(WebServer.HOME_URL); // redirects home
        return null;
    }
}
