package com.webcheckers.ui;

import java.util.*;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Player;
import spark.*;

import com.webcheckers.util.Message;

import static spark.Spark.halt;


/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 * @author Ash Nguyen
 * @author Nicholas Curl
 */
public class GetHomeRoute implements Route {

    /**
     * The logger of this class
     */
    static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    /**
     * The welcome message
     */
    static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");

    /**
     * The message to display when the player has signed in
     */
    static final Message OTHER_PLAYERS_MSG = Message.info("Click on one of these players to begin a game of checkers.");

    /**
     * The parameter pattern for the message
     */
    static final String MESSAGE_ATTR = "message";

    /**
     * The parameter pattern for the message's type
     */
    static final String MESSAGE_TYPE_ATTR = "messageType";

    /**
     * The pattern to set the message type to error
     */
    static final String ERROR_TYPE = "error";

    /**
     * The parameter pattern to get the message to display
     */
    static final String MESSAGE = "messageValue";

    /**
     * The parameter pattern for the title
     */
    static final String TITLE_ATTR = "title";

    /**
     * The value of the title for the home page
     */
    static final String TITLE = "Home";

    /**
     * The parameter pattern for the current user
     */
    static final String CURRENT_USER_ATTR = "currentUser";

    /**
     * The parameter pattern for the active players
     */
    private final String ACTIVE_PLAYERS = "activePlayers";

    /**
     * The parameter pattern for getting the currentPlayer
     */
    static final String CURRENT_PLAYER = "currentPlayer";

    /**
     * The game center from the server
     */
    private final GameCenter gameCenter;

    /**
     * The template engine from the server
     */
    private final TemplateEngine templateEngine;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param gameCenter     The instance of the GameCenter
     * @param templateEngine The HTML template rendering engine
     */
    public GetHomeRoute(final GameCenter gameCenter, final TemplateEngine templateEngine) {
        this.gameCenter = gameCenter;
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        //
        LOG.config("GetHomeRoute is initialized.");
    }

    /**
     * Render the WebCheckers Home page.
     *
     * @param request  The HTTP request
     * @param response The HTTP response
     * @return The rendered HTML for the Home page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetHomeRoute is invoked.");

        final Session httpSession = request.session();
        //
        Map<String, Object> vm = new HashMap<>();
        vm.put(TITLE_ATTR, TITLE);
        // display a user message in the Home page

        if (httpSession.attribute(CURRENT_PLAYER) != null) {
            final Player player = httpSession.attribute(CURRENT_PLAYER);

            if (player.isPlaying()) {
                String gameID = "";
                for (String keys : gameCenter.getGames().keySet()) { //Go through all the gameIDs and checks if player is in the game
                    if ((player.equals(gameCenter.getGame(keys).getRedPlayer()) || player.equals(gameCenter.getGame(keys).getWhitePlayer())) && !gameCenter.getGame(keys).isGameOver()) { //sets the gameID when its not a completed game as well
                        gameID = keys;
                        break;
                    }
                }
                response.redirect(WebServer.GAME_URL + "?gameID=" + gameID); //redirects other player to game with gameID
                halt();
                return null;
            } else {
                if (player.isReplaying()) { //set player replaying a game to false
                    player.setReplaying(false);
                }
                vm.remove(MESSAGE_ATTR, WELCOME_MSG);
                if (httpSession.attribute(MESSAGE) != null) { //display the error message
                    final String message = httpSession.attribute(MESSAGE);
                    vm.put(MESSAGE_ATTR, Message.error(message));
                    httpSession.attribute(MESSAGE, null);
                } else {
                    vm.put(MESSAGE_ATTR, OTHER_PLAYERS_MSG);
                }
                // print out the list of players
                vm.put(ACTIVE_PLAYERS, gameCenter.getPlayers());
                // remove current user from the list
                vm.put(CURRENT_USER_ATTR, player);
            }
        } else {   // show number of active players
            vm.put(MESSAGE_ATTR, WELCOME_MSG);
            vm.put("numPlayers", gameCenter.getPlayers().size());
        }
        // render the View
        return templateEngine.render(new ModelAndView(vm, "home.ftl"));
    }
}
