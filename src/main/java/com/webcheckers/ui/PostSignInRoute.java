package com.webcheckers.ui;

import com.webcheckers.appl.GameCenter;
import spark.*;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import static spark.Spark.halt;


/**
 * The UI Controller to POST the Sign In page after an acceptable username.
 *
 * @author Nicholas Curl
 * @author Ash Nguyen
 */
public class PostSignInRoute implements Route {

    /**
     * The logger of this class
     */
    private static final Logger LOG = Logger.getLogger(PostSignInRoute.class.getName());

    /**
     * The template engine from the server
     */
    private final TemplateEngine templateEngine;

    /**
     * The game center from the server
     */
    private final GameCenter gameCenter;

    /**
     * The parameter pattern of the username
     */
    static final String USERNAME_PARAM = "username";

    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /signIn} HTTP requests.
     *
     * @param gameCenter The instance of the GameCenter
     * @param templateEngine The HTML template rendering engine
     */
    public PostSignInRoute(GameCenter gameCenter, final TemplateEngine templateEngine){

        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");

        this.gameCenter = gameCenter;

        LOG.config("PostSignInRoute is initialized.");
    }

    /**
     * Render the WebCheckers Sign In page.
     *
     * @param request The HTTP request
     * @param response The HTTP response
     * @return The rendered HTML for the Sign In page
     */
    @Override
    public Object handle(Request request, Response response){
        final Session httpSession = request.session();
        LOG.finer("PostSignInRoute is invoked.");
        Map<String, Object> vm = new HashMap<>();
        vm.put(GetHomeRoute.TITLE_ATTR, "Sign In");

        if(httpSession.attribute(GetHomeRoute.CURRENT_PLAYER)== null) {
            String username = request.queryParams(USERNAME_PARAM);
            Pattern p = Pattern.compile("[^a-zA-Z0-9]");


            ModelAndView mv;

            // keyword to bypass and create add some players to the list
            if (username.equals("admin")) {
                gameCenter.addPlayer("legend69");
                gameCenter.addPlayer("nikki3413");
                gameCenter.addPlayer("hillary239");
            }
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
                vm.put(GetHomeRoute.CURRENT_USER_ATTR, gameCenter.getPlayer(username));
                httpSession.attribute(GetHomeRoute.CURRENT_PLAYER, gameCenter.getPlayer(username));
            }


            return templateEngine.render(new ModelAndView(vm, "signin.ftl"));
        }
        else{
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }
    }

    /**
     * The helper function for displaying an error message
     * @param vm The map of the view model
     * @param message The message to display
     * @return The rendered HTML for the Sign In page with an error message
     */
    private ModelAndView error(final Map<String, Object> vm, final String message) {
        vm.put(GetHomeRoute.MESSAGE_ATTR, message);
        vm.put(GetHomeRoute.MESSAGE_TYPE_ATTR, GetHomeRoute.ERROR_TYPE);
        return new ModelAndView(vm, "signin.ftl");
    }
}
