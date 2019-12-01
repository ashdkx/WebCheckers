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
 * @author Alec Jackson
 */
public class GetSpectateRoute implements Route {

    private static final Logger LOG = Logger.getLogger(GetReplayRoute.class.getName());

    static final String TITLE = "Replay";
    private final GameCenter gameCenter;

    private final TemplateEngine templateEngine;


    public GetSpectateRoute(final GameCenter gameCenter, final TemplateEngine templateEngine) {
        this.gameCenter = gameCenter;
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        //
        LOG.config("GetSpectateRoute is initialized.");
    }

    @Override
    public Object handle(Request request, Response response) {
        final Session httpSession = request.session();
        LOG.finer("GetSpectateRoute is invoked.");
        //
        Map<String, Object> vm = new HashMap<>();
        vm.put(GetHomeRoute.TITLE_ATTR, TITLE);


        if(httpSession.attribute(GetHomeRoute.CURRENT_PLAYER) != null){
            final Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);
            player.setSpectating(true);
            if(gameCenter.getCurrentGames().isEmpty()){ //checks to see if there are any games saved, redirects back home if there aren't any
                response.redirect(WebServer.HOME_URL);
                halt();
                return null;
            }
            vm.put(GetHomeRoute.CURRENT_USER_ATTR, player);
            vm.put("currentGames", gameCenter.getCurrentGames()); //display the list of saved games
            if(httpSession.attribute(GetHomeRoute.MESSAGE)!=null){ //display a message if necessary
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
