package com.webcheckers.ui;

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


    public PostSignInRoute(final TemplateEngine templateEngine){

        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        
        LOG.config("PostSignInRoute is initialized.");


    }


    @Override
    public Object handle(Request request, Response response){

        LOG.finer("GetHomeRoute is invoked.");
        Map<String, Object> vm = new HashMap<>();

        vm.put("title", "Sign In");


        return templateEngine.render(new ModelAndView(vm , "signin.ftl"));

    }
}
