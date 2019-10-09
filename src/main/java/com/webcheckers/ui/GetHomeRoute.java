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
 */
public class GetHomeRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

  private static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");
  private static final Message OTHER_PLAYERS_MSG = Message.info("Click on one of these players to begin a game of checkers.");
  private final GameCenter gameCenter;

  private final String ACTIVE_PLAYERS = "activePlayers";
  static final String CURRENT_PLAYER = "currentPlayer";

  private final TemplateEngine templateEngine;

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
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
   * @param request
   *   the HTTP request
   * @param response
   *   the HTTP response
   *
   * @return
   *   the rendered HTML for the Home page
   */
  @Override
  public Object handle(Request request, Response response) {
    final Session httpSession = request.session();
    LOG.finer("GetHomeRoute is invoked.");
    //
    Map<String, Object> vm = new HashMap<>();
    vm.put("title", "Welcome!");

    // display a user message in the Home page


    if(httpSession.attribute(CURRENT_PLAYER) != null){
      final Player player = httpSession.attribute(CURRENT_PLAYER);
      System.out.println(player.getName());
      vm.put("currentPlayer",player);
      vm.remove("message",WELCOME_MSG);
      vm.put("message",OTHER_PLAYERS_MSG);
      // print out the list of players
      vm.put(ACTIVE_PLAYERS, gameCenter.getPlayers());
    }
    else{
      vm.put("message", WELCOME_MSG);

    }


    // render the View
    return templateEngine.render(new ModelAndView(vm , "home.ftl"));
  }
}
