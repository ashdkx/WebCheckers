package com.webcheckers.ui;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Player;
import spark.*;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;


/**
 * @author Nicholas Curl
 */
public class PostSignInRoute implements Route {

    private static final Logger LOG = Logger.getLogger(GetSignInRoute.class.getName());

    private final TemplateEngine templateEngine;
    private final GameCenter gameCenter;

    static final String MESSAGE_ATTR = "message";
    static final String MESSAGE_TYPE_ATTR = "messageType";
    static final String ERROR_TYPE = "error";

    static final String USERNAME_PARAM = "username";


    public PostSignInRoute(GameCenter gameCenter, final TemplateEngine templateEngine){

        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");

        this.gameCenter = gameCenter;

        LOG.config("PostSignInRoute is initialized.");
      

    }


    @Override
    public Object handle(Request request, Response response){
        final Session httpSession = request.session();
        LOG.finer("GetHomeRoute is invoked.");
        Map<String, Object> vm = new HashMap<>();

        vm.put("title", "Sign In");
        if(httpSession.attribute(GetHomeRoute.CURRENT_PLAYER)== null) {
            String username = request.queryParams(USERNAME_PARAM);


            ModelAndView mv;
            if (gameCenter.getPlayers().containsKey(username)) {
                mv = error(vm, "Username exists");
                return templateEngine.render(mv);
            } else if (username.isEmpty()) {
                mv = error(vm, "Please enter a valid character");
                return templateEngine.render(mv);
            } else {
                gameCenter.addPlayer(username);
                vm.put("currentUser", gameCenter.getPlayer(username));
                httpSession.attribute(GetHomeRoute.CURRENT_PLAYER, gameCenter.getPlayer(username));
                Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);
                if (player == null) {
                    response.redirect(WebServer.HOME_URL);
                    halt();
                    return null;
                }
            }


            return templateEngine.render(new ModelAndView(vm, "signin.ftl"));
        }
        else{
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }
    }

    private ModelAndView error(final Map<String, Object> vm, final String message) {
        vm.put(MESSAGE_ATTR, message);
        vm.put(MESSAGE_TYPE_ATTR, ERROR_TYPE);
        return new ModelAndView(vm, "signin.ftl");
    }
}
