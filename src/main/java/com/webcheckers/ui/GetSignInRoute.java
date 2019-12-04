package com.webcheckers.ui;

import com.webcheckers.model.Player;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;



/**
 * The UI Controller to GET the Sign in page.
 *
 * @author Nicholas Curl
 */
public class GetSignInRoute implements Route {

    /**
     * The logger of this class
     */
    private static final Logger LOG = Logger.getLogger(GetSignInRoute.class.getName());

    /**
     * The template engine from the server
     */
    private final TemplateEngine templateEngine;

    /**
     * The value of this route's title
     */
    static final String TITLE = "Sign In";

    /**
     * The value of the template to render
     */
    static final String VIEW_NAME = "signin.ftl";

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /signIn} HTTP requests.
     *
     * @param templateEngine The HTML template rendering engine
     */
    public GetSignInRoute(final TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        LOG.config("GetSignInRoute is initialized.");

    }

    /**
     * Render the WebCheckers Sign In page.
     *
     * @param request  The HTTP request
     * @param response The HTTP response
     * @return The rendered HTML for the Sign In page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetSignInRoute is invoked.");

        final Session httpSession = request.session();

        final Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);
        if (player == null) { //Checks to see if the current user is null
            final Map<String, Object> vm = new HashMap<>();
            vm.put(GetHomeRoute.TITLE_ATTR, TITLE);
            return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
        } else {
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }
    }
}
