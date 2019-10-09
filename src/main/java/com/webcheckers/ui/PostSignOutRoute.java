package com.webcheckers.ui;

import com.webcheckers.appl.GameCenter;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

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

        LOG.finer("GetHomeRoute is invoked.");
        Map<String, Object> vm = new HashMap<>();

        vm.put("title", "Sign Out");

        vm.put("currentUser",null);
        gameCenter.removeCurrentUser();


        return templateEngine.render(new ModelAndView(vm , "home.ftl"));

    }


}