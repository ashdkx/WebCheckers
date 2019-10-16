package com.webcheckers.ui;

import com.webcheckers.appl.GameBoard;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.GameView;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;

/**
 * The UI Controller to GET the Game page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 * @author Nicholas Curl
 */
public class GetGameRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetGameRoute.class.getName());

  private static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");
  private static final Message OTHER_PLAYERS_MSG = Message.info("Click on one of these players to begin a game of checkers.");
  static final String PLAYER_PARAM = "player";
  private final GameCenter gameCenter;
  private enum color{
    RED,
    WHITE
  }

  private enum mode {
    PLAY,
    SPECTATOR,
    REPLAY
  }
  private final TemplateEngine templateEngine;

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetGameRoute(final GameCenter gameCenter, final TemplateEngine templateEngine) {
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
   *   the rendered HTML for the Game page
   */
  @Override
  public Object handle(Request request, Response response) {
    LOG.finer("GetGameRoute is invoked.");
    final Session httpSession = request.session();

    Map<String,Object> modeOptions = new HashMap<>();
    modeOptions.put(null,null);

    Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);
    Map<String, Object> vm = new HashMap<>();


    if(!player.isPlaying()) {
      Player player2 = gameCenter.getPlayer(request.queryParams(PLAYER_PARAM));
      if (player2.isPlaying()){
        httpSession.attribute(GetHomeRoute.MESSAGE,"Player already in game. Click a different player to begin a game of checkers.");
        response.redirect(WebServer.HOME_URL);
        return null;
      }
      gameCenter.setPlayer1(player, true);
      gameCenter.setPlaying(player, true);
      gameCenter.setPlaying(player2, true);
      GameBoard board = new GameBoard(player, player2);
      gameCenter.setGame(player, board);
      gameCenter.setGame(player2, board);
    }

    vm.put("title", "Checkers");
    vm.put("currentUser", player);
    vm.put("viewMode", mode.PLAY);
    //vm.put("modeOptions", modeOptions);



    if(player.isPlayer1()) {
      //
      gameCenter.getGame(player).setPlayer2Board(false);
      vm.put("redPlayer", player);
      vm.put("whitePlayer", gameCenter.getGame(player).getPlayer2());
      System.out.println("Player2: "+gameCenter.getGame(player).getPlayer2().getName());
      vm.put("activeColor", color.RED);
      vm.put("board", gameCenter.getGame(player));
    }

    else {
      gameCenter.getGame(player).setPlayer2Board(true);
      vm.put("redPlayer",gameCenter.getGame(player).getPlayer1());
      vm.put("whitePlayer", player);
      vm.put("activeColor", color.RED);
      vm.put("board", gameCenter.getGame(player));
    }



    // render the View
    return templateEngine.render(new ModelAndView(vm , "game.ftl"));
  }

}

