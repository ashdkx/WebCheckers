package com.webcheckers.ui;

import com.webcheckers.appl.GameCenter;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;


/**
 * @author Nicholas Curl
 */
public class PostSignInRoute implements Route {

    private static final Logger LOG = Logger.getLogger(GetSignInRoute.class.getName());

    private final TemplateEngine templateEngine;
    private final GameCenter gameCenter;
    private final String MESSAGE_ATTR = "message";
    private final String MESSAGE_TYPE_ATTR = "messageType";
    private final String ERROR_TYPE = "error";

    static final String USERNAME_PARAM = "username";


    public PostSignInRoute(GameCenter gameCenter, final TemplateEngine templateEngine){

        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");

        this.gameCenter = gameCenter;

        LOG.config("PostSignInRoute is initialized.");
      

    }


    @Override
    public Object handle(Request request, Response response){

        LOG.finer("GetHomeRoute is invoked.");
        Map<String, Object> vm = new HashMap<>();

        String username = request.queryParams(USERNAME_PARAM);

        //gameCenter.addPlayer("legend69");
        //gameCenter.addPlayer("ashdkx");

        vm.put("title", "Sign In");

        //check valid username

        ModelAndView mv;
        if (gameCenter.getPlayers().isEmpty()) {
            gameCenter.addPlayer(username);
        } else if (gameCenter.getPlayers().containsKey(username)) {
                mv = error(vm, "Username exists");
                return templateEngine.render(mv);

        } else {
            gameCenter.addPlayer(username);
            vm.put("currentUser",gameCenter.getPlayer(username));
            gameCenter.setCurrentUser(gameCenter.getPlayer(username));
        }
        return templateEngine.render(new ModelAndView(vm , "signin.ftl"));

    }

    private ModelAndView error(final Map<String, Object> vm, final String message) {
        vm.put(MESSAGE_ATTR, message);
        vm.put(MESSAGE_TYPE_ATTR, ERROR_TYPE);
        return new ModelAndView(vm, "signin.ftl");
    }
}
