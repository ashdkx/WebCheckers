package com.webcheckers.ui;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Player;
import com.webcheckers.model.SavedGame;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;


public class GetReplayStopWatchRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetReplayStopWatchRoute.class.getName());

    static final String TITLE = "Replay Exit";
    private final GameCenter gameCenter;


    public GetReplayStopWatchRoute(final GameCenter gameCenter) {
        this.gameCenter = gameCenter;
        //
        LOG.config("GetReplayStopWatchRoute is initialized.");
    }

    /**
     * Render the WebCheckers Home page.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the Home page
     */
    @Override
    public Object handle(Request request, Response response) {
        final Session httpSession = request.session();
        LOG.finer("GetReplayStopWatchRoute is invoked.");
        //
        Map<String, Object> vm = new HashMap<>();
        vm.put(GetHomeRoute.TITLE_ATTR, TITLE);

        final Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);
        final SavedGame savedGame = gameCenter.getGameSave(request.queryParams(GetGameRoute.GAMEID_PARAM));
        savedGame.resetSavedGame();
        player.setReplaying(false);
        vm.put(GetHomeRoute.CURRENT_USER_ATTR,player);
        response.redirect(WebServer.HOME_URL);
        return null;
    }
}
