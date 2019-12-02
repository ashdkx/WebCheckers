package com.webcheckers.ui;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;

/**
 * The UI Controller to GET the Replay page.
 *
 * @author Nicholas Curl
 */
public class GetReplayRoute implements Route {

    /**
     * The logger of this class
     */
    private static final Logger LOG = Logger.getLogger(GetReplayRoute.class.getName());

    /**
     * The value of this route's title
     */
    static final String TITLE = "Replay";

    /**
     * The game center from the server
     */
    private final GameCenter gameCenter;

    /**
     * The template engine from the server
     */
    private final TemplateEngine templateEngine;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /replay} HTTP requests.
     *
     * @param gameCenter     The instance of the GameCenter
     * @param templateEngine The HTML template rendering engine
     */
    public GetReplayRoute(final GameCenter gameCenter, final TemplateEngine templateEngine) {
        this.gameCenter = gameCenter;
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        //
        LOG.config("GetReplayRoute is initialized.");
    }

    /**
     * Render the WebCheckers Replay page.
     *
     * @param request  The HTTP request
     * @param response The HTTP response
     * @return The rendered HTML for the Replay page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetReplayRoute is invoked.");

        final Session httpSession = request.session();
        //
        Map<String, Object> vm = new HashMap<>();
        vm.put(GetHomeRoute.TITLE_ATTR, TITLE);

        if (httpSession.attribute(GetHomeRoute.CURRENT_PLAYER) != null) {
            final Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);
            player.setReplaying(true);
            if (gameCenter.getSavedGames().isEmpty()) { //checks to see if there are any games saved, redirects back home if there aren't any
                response.redirect(WebServer.HOME_URL);
                halt();
                return null;
            }
            vm.put(GetHomeRoute.CURRENT_USER_ATTR, player);
            vm.put("savedGames", gameCenter.getSavedGames()); //display the list of saved games
            if (httpSession.attribute(GetHomeRoute.MESSAGE) != null) { //display a message if necessary
                final String message = httpSession.attribute(GetHomeRoute.MESSAGE);
                vm.put(GetHomeRoute.MESSAGE_ATTR, Message.error(message));
                httpSession.attribute(GetHomeRoute.MESSAGE, null);
            }
        } else {
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }
        // render the View
        return templateEngine.render(new ModelAndView(vm, "replay.ftl"));
    }
}
