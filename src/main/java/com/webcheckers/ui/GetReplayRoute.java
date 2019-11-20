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

public class GetReplayRoute implements Route {

    private static final Logger LOG = Logger.getLogger(GetReplayRoute.class.getName());

    static final String TITLE = "Replay";
    private final GameCenter gameCenter;

    private final TemplateEngine templateEngine;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public GetReplayRoute(final GameCenter gameCenter, final TemplateEngine templateEngine) {
        this.gameCenter = gameCenter;
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        //
        LOG.config("GetReplayRoute is initialized.");
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
        LOG.finer("GetReplayRoute is invoked.");
        //
        Map<String, Object> vm = new HashMap<>();
        vm.put(GetHomeRoute.TITLE_ATTR, TITLE);
        // display a user message in the Home page

        // show active players
        if(httpSession.attribute(GetHomeRoute.CURRENT_PLAYER) != null){
            final Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);
            player.setReplaying(true);
            vm.put(GetHomeRoute.CURRENT_USER_ATTR, player);
            vm.put("savedGames", gameCenter.getSavedGames());
            if(httpSession.attribute(GetHomeRoute.MESSAGE)!=null){
                final String message = httpSession.attribute(GetHomeRoute.MESSAGE);
                vm.put(GetHomeRoute.MESSAGE_ATTR, Message.error(message));
                httpSession.attribute(GetHomeRoute.MESSAGE, null);
            }
        }
        else{
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }


        // render the View
        return templateEngine.render(new ModelAndView(vm , "replay.ftl"));
    }
}
