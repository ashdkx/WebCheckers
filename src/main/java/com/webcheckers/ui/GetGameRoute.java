package com.webcheckers.ui;

import com.webcheckers.appl.GameBoard;
import com.webcheckers.appl.GameCenter;
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


  public enum mode {
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


    Map<String, Object> vm = new HashMap<>();

    if (httpSession.attribute(GetHomeRoute.CURRENT_PLAYER) != null) {
      Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);
      if (!player.isPlaying()) {
        Player player2 = gameCenter.getPlayer(request.queryParams(PLAYER_PARAM));
        if (player2.isPlaying()) {
          httpSession.attribute(GetHomeRoute.MESSAGE, "Player already in game. Click a different player to begin a game of checkers.");
          response.redirect(WebServer.HOME_URL);
          return null;
        }
        GameBoard board = new GameBoard(player, player2);
        player.setRedPlayer(true);
        player.setPlaying(true);
        player.setColor(GameBoard.color.RED);
        player.setMyTurn(true);
        player.setGame(board);
        player2.setColor(GameBoard.color.WHITE);
        player2.setPlaying(true);
        player2.setGame(board);
        vm.put("activeColor", GameBoard.color.RED);
      }

      vm.put("title", "Checkers");
      vm.put("currentUser", player);
      vm.put("viewMode", mode.PLAY);


      Player player2 = null;
      if (player.isRedPlayer()) {
        GameBoard board = player.getGame();
        board.isWhitePlayerBoard(false);
        vm.put("redPlayer", player);
        vm.put("whitePlayer", board.getWhitePlayer());
        vm.put("board", board);
        player2 = board.getWhitePlayer();


      } else {
        GameBoard board = player.getGame();
        board.isWhitePlayerBoard(true);
        vm.put("redPlayer", board.getRedPlayer());
        vm.put("whitePlayer", player);
        vm.put("board", board);
        player2 = board.getRedPlayer();
      }

      if (player.isMyTurn()) {
        vm.put("activeColor", player.getColor());
      } else {
        vm.put("activeColor", player2.getColor());
      }

      // render the View
      return templateEngine.render(new ModelAndView(vm, "game.ftl"));
    }
    else {
      response.redirect(WebServer.HOME_URL);
      halt();
      return null;
    }
  }

}

