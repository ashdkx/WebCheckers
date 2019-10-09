package com.webcheckers.ui;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Player;
import com.webcheckers.model.PlayerLobby;
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


        if(httpSession.attribute(GetHomeRoute.CURRENT_PLAYER)== null) {
            String username = request.queryParams(USERNAME_PARAM);
            gameCenter.addPlayer(username);


            vm.put("title", "Sign In");

            vm.put("currentUser", gameCenter.getPlayer(username));
            httpSession.attribute(GetHomeRoute.CURRENT_PLAYER, gameCenter.getPlayer(username));
            Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);
            if (player == null) {
                response.redirect(WebServer.HOME_URL);
                halt();
                return null;
            }
            return templateEngine.render(new ModelAndView(vm, "signin.ftl"));
        }

        else{
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }
    }
}
