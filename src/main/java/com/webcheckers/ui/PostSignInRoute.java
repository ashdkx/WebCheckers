package com.webcheckers.ui;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Player;
import spark.*;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import static spark.Spark.halt;


/**
 * @author Nicholas Curl
 * @author Ash Nguyen
 */
public class PostSignInRoute implements Route {

    private static final Logger LOG = Logger.getLogger(GetSignInRoute.class.getName());

    private final TemplateEngine templateEngine;
    private final GameCenter gameCenter;


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
        Pattern p = Pattern.compile("[^a-zA-Z0-9]");
        vm.put("title", "Sign In");

        if(httpSession.attribute(GetHomeRoute.CURRENT_PLAYER)== null) {
            String username = request.queryParams(USERNAME_PARAM);
            Pattern p = Pattern.compile("[^a-zA-Z0-9]");


            ModelAndView mv;

            // keyword to bypass and create add some players to the list
            if (username.equals("admin")) {
                gameCenter.addPlayer("legend69");
                gameCenter.addPlayer("nikki3413");
                gameCenter.addPlayer("hillary239");
            } else
            //check if username input exists or not
            if (gameCenter.getPlayers().containsKey(username)) {
                mv = error(vm, "Username exists");
                return templateEngine.render(mv);
            } else if (username.isEmpty() || p.matcher(username).find()) {
                mv = error(vm, "Please enter a valid username");
                return templateEngine.render(mv);
            // check if username input is not empty
            } else if (username.isEmpty() || p.matcher(username).find()) {
                mv = error(vm, "Please enter a valid character");
                return templateEngine.render(mv);
            // add new user to the list if it's valid
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
        vm.put(GetHomeRoute.MESSAGE_ATTR, message);
        vm.put(GetHomeRoute.MESSAGE_TYPE_ATTR, GetHomeRoute.ERROR_TYPE);
        return new ModelAndView(vm, "signin.ftl");
    }
}
