package com.webcheckers.ui;

import static spark.Spark.*;

import java.util.Objects;
import java.util.logging.Logger;

import com.google.gson.Gson;

import com.webcheckers.appl.GameCenter;
import spark.TemplateEngine;


/**
 * The server that initializes the set of HTTP request handlers.
 * This defines the <em>web application interface</em> for this
 * WebCheckers application.
 *
 * <p>
 * There are multiple ways in which you can have the client issue a
 * request and the application generate responses to requests. If your team is
 * not careful when designing your approach, you can quickly create a mess
 * where no one can remember how a particular request is issued or the response
 * gets generated. Aim for consistency in your approach for similar
 * activities or requests.
 *
 *
 * <p>Design choices for how the client makes a request include:
 * <ul>
 *     <li>Request URL</li>
 *     <li>HTTP verb for request (GET, POST, PUT, DELETE and so on)</li>
 *     <li><em>Optional:</em> Inclusion of request parameters</li>
 * </ul>
 *
 *
 * <p>Design choices for generating a response to a request include:
 * <ul>
 *     <li>View templates with conditional elements</li>
 *     <li>Use different view templates based on results of executing the client request</li>
 *     <li>Redirecting to a different application URL</li>
 * </ul>
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 * @author Nicholas Curl
 */
public class WebServer {
    /**
     * The logger of this class
     */
    private static final Logger LOG = Logger.getLogger(WebServer.class.getName());

    //
    // Constants
    //

    /**
     * The URL pattern to request the Home page.
     */
    public static final String HOME_URL = "/";

    /**
     * The URL pattern to request the Sign In page.
     */
    public static final String SIGNIN_URL = "/signin";

    /**
     * The URL pattern to request the Sign Out page.
     */
    public static final String SIGNOUT_URL = "/signout";

    /**
     * The URL pattern to request the Game page.
     */
    public static final String GAME_URL = "/game";

    /**
     * The URL pattern to request the Validate Move Ajax request.
     */
    public static final String VALIDATEMOVE_URL = "/validateMove";

    /**
     * The URL pattern to request the Backup Move Ajax request.
     */
    public static final String BACKUPMOVE_URL = "/backupMove";

    /**
     * The URL pattern to request the Check Turn Ajax request.
     */
    public static final String CHECKTURN_URL = "/checkTurn";

    /**
     * The URL pattern to request the Submit Turn Ajax request.
     */
    public static final String SUBMITTURN_URL = "/submitTurn";

    /**
     * The URL pattern to request the Resign Game Ajax request.
     */
    public static final String RESIGN_URL = "/resignGame";

    /**
     * The URL pattern to request the Replay page.
     */
    public static final String REPLAYMODE_URL = "/replay";

    /**
     * The URL pattern to request the Replay Game page.
     */
    public static final String REPLAYGAME_URL = "/replay/game";

    /**
     * The URL pattern to request the Replay Next Turn Ajax request.
     */
    public static final String NEXTTURN_URL = "/replay/nextTurn";

    /**
     * The URL pattern to request the Replay Previous Turn Ajax request.
     */
    public static final String PREVTURN_URL = "/replay/previousTurn";

    /**
     * The URL pattern to request the Replay Stop Watching page.
     */
    public static final String STOPREPLAY_URL = "/replay/stopWatching";

    /**
     * The URL pattern to request the Spectator page.
     */
    public static final String SPECTATEMODE_URL = "/spectator";

    /**
     * The URL pattern to request the Spectator Game page.
     */
    public static final String SPECTATEGAME_URL ="/spectator/game";

    /**
     * The URL pattern to stop spectating
     */
    public static final String SPECTATESTOP_URL = "/spectator/stopWatching";

    /**
     * The URL pattern to check for another turn in spectate mode
     */
    public static final String SPECTATECHECK_URL = "/spectator/checkTurn";
    //
    // Attributes
    //

    /**
     * The HTML template rendering engine.
     */
    private final TemplateEngine templateEngine;

    /**
     * An instance of the Gson class.
     */
    private final Gson gson;

    /**
     * An instance of the game center.
     */
    private final GameCenter gameCenter;

    //
    // Constructor
    //

    /**
     * The constructor for the Web Server.
     *
     * @param templateEngine The default {@link TemplateEngine} to render page-level HTML views.
     * @param gson           The Google JSON parser object used to render Ajax responses.
     * @param gameCenter     The game center instance
     * @throws NullPointerException If any of the parameters are {@code null}.
     */
    public WebServer(final TemplateEngine templateEngine, final Gson gson, final GameCenter gameCenter) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(gameCenter, "gameCenter must not be null");
        Objects.requireNonNull(gson, "gson must not be null");
        //
        this.templateEngine = templateEngine;
        this.gameCenter = gameCenter;
        this.gson = gson;
    }

    //
    // Public methods
    //

    /**
     * Initialize all of the HTTP routes that make up this web application.
     *
     * <p>
     * Initialization of the web server includes defining the location for static
     * files, and defining all routes for processing client requests. The method
     * returns after the web server finishes its initialization.
     * </p>
     */
    public void initialize() {

        // Configuration to serve static files
        staticFileLocation("/public");

        //// Setting any route (or filter) in Spark triggers initialization of the
        //// embedded Jetty web server.

        //// A route is set for a request verb by specifying the path for the
        //// request, and the function callback (request, response) -> {} to
        //// process the request. The order that the routes are defined is
        //// important. The first route (request-path combination) that matches
        //// is the one which is invoked. Additional documentation is at
        //// http://sparkjava.com/documentation.html and in Spark tutorials.

        //// Each route (processing function) will check if the request is valid
        //// from the client that made the request. If it is valid, the route
        //// will extract the relevant data from the request and pass it to the
        //// application object delegated with executing the request. When the
        //// delegate completes execution of the request, the route will create
        //// the parameter map that the response template needs. The data will
        //// either be in the value the delegate returns to the route after
        //// executing the request, or the route will query other application
        //// objects for the data needed.

        //// FreeMarker defines the HTML response using templates. Additional
        //// documentation is at
        //// http://freemarker.org/docs/dgui_quickstart_template.html.
        //// The Spark FreeMarkerEngine lets you pass variable values to the
        //// template via a map. Additional information is in online
        //// tutorials such as
        //// http://benjamindparrish.azurewebsites.net/adding-freemarker-to-java-spark/.

        //// These route definitions are examples. You will define the routes
        //// that are appropriate for the HTTP client interface that you define.
        //// Create separate Route classes to handle each route; this keeps your
        //// code clean; using small classes.

        // Shows the Checkers game Home page.
        get(HOME_URL, new GetHomeRoute(gameCenter, templateEngine));

        //Show the Checkers sign in page.
        get(SIGNIN_URL, new GetSignInRoute(templateEngine));

        //Shows the Checkers game page
        get(GAME_URL, new GetGameRoute(gameCenter, templateEngine, gson));

        //Handles the sign in action
        post(SIGNIN_URL, new PostSignInRoute(gameCenter, templateEngine));

        //Handles the sign out action
        post(SIGNOUT_URL, new PostSignOutRoute(gameCenter));

        //Handles the Ajax request for validating a move
        post(VALIDATEMOVE_URL, new PostValidateMoveRoute(gameCenter, gson));

        //Handles the Ajax request for backing up a move
        post(BACKUPMOVE_URL, new PostBackupMoveRoute(gameCenter, gson));

        //Handles the Ajax request for checking the player's turn
        post(CHECKTURN_URL, new PostCheckTurnRoute(gameCenter, gson));

        //Handles the Ajax request for submitting a turn
        post(SUBMITTURN_URL, new PostSubmitTurnRoute(gameCenter, gson));

        //Handles the Ajax request for resigning from the game
        post(RESIGN_URL, new PostResignGameRoute(gameCenter, gson));

        //Shows the Checkers replay page for selecting game to replay
        get(REPLAYMODE_URL, new GetReplayRoute(gameCenter, templateEngine));

        //Shows the Checkers replay game page of the game being replayed
        get(REPLAYGAME_URL, new GetReplayGameRoute(gameCenter, templateEngine, gson));

        //Handles the Ajax request for going to the next turn in replay mode
        post(NEXTTURN_URL, new PostNextTurnRoute(gameCenter, gson));

        //Handles the Ajax request for going to the previous turn in replay mode
        post(PREVTURN_URL, new PostPreviousTurnRoute(gameCenter, gson));

        //Handles the player that stops watching a replayed game
        get(STOPREPLAY_URL, new GetReplayStopWatchRoute(gameCenter));

        //Shows the spectator game page
        get(SPECTATEGAME_URL, new GetSpectateGameRoute(gameCenter,templateEngine,gson));
        //Shows the spectator game select page
        get(SPECTATEMODE_URL, new GetSpectateRoute(gameCenter,templateEngine));
        //Handles a request to stop spectating
        get(SPECTATESTOP_URL, new GetSpectateStopWatchingRoute(gameCenter));
        //Checks for a turn progressed in the game being spectated
        post(SPECTATECHECK_URL, new PostSpectateCheckTurnRoute(gameCenter,gson) );
        //
        LOG.config("WebServer is initialized.");
    }

}