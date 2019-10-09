package com.webcheckers.ui;

import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;


/**
 * @author Nicholas Curl
 */
public class GetSignInRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetSignInRoute.class.getName());

    private final TemplateEngine templateEngine;


    public GetSignInRoute(final TemplateEngine templateEngine){
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        LOG.config("GetSignInRoute is initialized.");

    }


    @Override
    public Object handle(Request request, Response response){
        final Session httpSession = request.session();
        LOG.finer("GetHomeRoute is invoked.");
        Map<String, Object> vm = new HashMap<>();
        final Session httpSession = request.session();
        vm.put("title", "Sign In");

        if(httpSession.attribute(GetHomeRoute.CURRENT_PLAYER)== null) {
            return templateEngine.render(new ModelAndView(vm, "signin.ftl"));
        }
        else{
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }
    }
}
