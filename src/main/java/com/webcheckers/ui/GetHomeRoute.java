package com.webcheckers.ui;

import java.util.*;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Player;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

import com.webcheckers.util.Message;

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
  private final String CURRENT_PLAYER = "currentPlayer";

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
    LOG.finer("GetHomeRoute is invoked.");
    //
    Map<String, Object> vm = new HashMap<>();
    vm.put("title", "Welcome!");

    // display a user message in the Home page
    if (gameCenter.getPlayers().size()>0){
        vm.put("currentUser",gameCenter.getCurrentUser());
    }

    if(gameCenter.getCurrentUser() != null){
      vm.remove("message",WELCOME_MSG);
      vm.put("message",OTHER_PLAYERS_MSG);
      vm.put(ACTIVE_PLAYERS, gameCenter.getPlayers());
      vm.put(CURRENT_PLAYER, gameCenter.getCurrentUser().getName());

    }
    else{
      vm.put("message", WELCOME_MSG);

    }


    // render the View
    return templateEngine.render(new ModelAndView(vm , "home.ftl"));
  }
}
