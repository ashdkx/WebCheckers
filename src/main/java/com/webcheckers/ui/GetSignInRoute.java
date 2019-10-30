package com.webcheckers.ui;

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
public class GetSignInRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetSignInRoute.class.getName());

    private final TemplateEngine templateEngine;
    static final String TITLE = "Sign In";
    static final String VIEW_NAME = "signin.ftl";

    public GetSignInRoute(final TemplateEngine templateEngine){
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        LOG.config("GetSignInRoute is initialized.");

    }


    @Override
    public Object handle(Request request, Response response){
        final Session httpSession = request.session();
        LOG.finer("GetSignInRoute is invoked.");

        final Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);
        if(player == null) {
            final Map<String, Object> vm = new HashMap<>();
            vm.put(GetHomeRoute.TITLE_ATTR, TITLE);
            return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
        }
        else{
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }
    }
}
