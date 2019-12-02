package com.webcheckers.ui;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Player;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;


/**
 * The UI Controller to POST the Sign Out page.
 *
 * @author Nicholas Curl
 */
public class PostSignOutRoute implements Route {

    /**
     * The logger of this class
     */
    private static final Logger LOG = Logger.getLogger(PostSignOutRoute.class.getName());

    /**
     * The game center from the server
     */
    private final GameCenter gameCenter;

    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /signOut} HTTP requests.
     *
     * @param gameCenter The instance of the GameCenter
     */
    public PostSignOutRoute(GameCenter gameCenter) {
        this.gameCenter = gameCenter;
        LOG.config("PostSignOutRoute is initialized.");
    }

    /**
     * Render the WebCheckers Sign Out page.
     *
     * @param request  The HTTP request
     * @param response The HTTP response
     * @return Null
     */
    @Override
    public Object handle(Request request, Response response) {

        final Session httpSession = request.session();

        LOG.finer("PostSignOutRoute is invoked.");
        Map<String, Object> vm = new HashMap<>();

        vm.put(GetHomeRoute.TITLE_ATTR, "Sign Out");
        Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);
        httpSession.attribute(GetHomeRoute.CURRENT_PLAYER, null);
        gameCenter.removePlayer(player.getName()); //removes the player

        vm.put(GetHomeRoute.CURRENT_USER_ATTR, null);

        response.redirect(WebServer.HOME_URL); //redirect home
        return null;
    }
}
