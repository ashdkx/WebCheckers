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
public class PostSignOutRoute implements Route {

    private static final Logger LOG = Logger.getLogger(GetSignInRoute.class.getName());

    private final TemplateEngine templateEngine;
    private final GameCenter gameCenter;


    public PostSignOutRoute(GameCenter gameCenter, final TemplateEngine templateEngine){

        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");

        this.gameCenter = gameCenter;

        LOG.config("PostSignOutRoute is initialized.");


    }



    @Override
    public Object handle(Request request, Response response){

        final Session httpSession = request.session();

        LOG.finer("GetHomeRoute is invoked.");
        Map<String, Object> vm = new HashMap<>();

        vm.put("title", "Sign Out");
        Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);
        httpSession.attribute(GetHomeRoute.CURRENT_PLAYER,null);
        gameCenter.removePlayer(player.getName());

        vm.put("currentUser",null);


        response.redirect(WebServer.HOME_URL);
        halt();
        return null;

    }


}
