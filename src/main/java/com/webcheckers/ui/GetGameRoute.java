package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameBoard;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

import static spark.Spark.halt;
import static spark.Spark.threadPool;

/**
 * The UI Controller to GET the Game page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 * @author Nicholas Curl
 */
public class GetGameRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetGameRoute.class.getName());

  static final String PLAYER_PARAM = "player";
  static final String GAMEID_PARAM = "gameID";
  static final String REDPLAYER_PARAM = "redPlayer";
  static final String WHITEPLAYER_PARAM = "whitePlayer";
  static final String VIEWMODE_PARAM = "viewMode";
  static final String BOARD_PARAM = "board";
  static final String MODEOPTIONS_PARAM = "modeOptionsAsJSON";
  static final String ACTIVECOLOR_PARAM = "activeColor";
  private final GameCenter gameCenter;


  public enum mode {
    PLAY,
    SPECTATOR,
    REPLAY
  }
  private final TemplateEngine templateEngine;
  private final Gson gson;
  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetGameRoute(final GameCenter gameCenter, final TemplateEngine templateEngine, final Gson gson) {
    this.gameCenter = gameCenter;
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    this.gson = gson;
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
    final Map<String, Object> modeOptions = new HashMap<>(2);
    Map<String, Object> vm = new HashMap<>();

    if (httpSession.attribute(GetHomeRoute.CURRENT_PLAYER) != null) {
      Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);
      String gameId = request.queryParams(GAMEID_PARAM); //Requests gameID param
      vm.put(GetHomeRoute.TITLE_ATTR, "Checkers");
      vm.put(GetHomeRoute.CURRENT_USER_ATTR, player);

      if(gameId == null) { //checks to see if the gameID is null

        if (!player.isPlaying()) {
          Player player2 = gameCenter.getPlayer(request.queryParams(PLAYER_PARAM));
          if (player2.isPlaying()) {
            httpSession.attribute(GetHomeRoute.MESSAGE, "Player already in game. Click a different player to begin a game of checkers.");
            response.redirect(WebServer.HOME_URL);
            return null;
          }
          if(player2.isReplaying()){
            httpSession.attribute(GetHomeRoute.MESSAGE, "Player is replaying a game. Click a different player to begin a game of checkers.");
            response.redirect(WebServer.HOME_URL);
            return null;
          }
          UUID uuid = UUID.randomUUID(); //Generates a UUID
          gameId = uuid.toString();
          gameCenter.addNewGame(gameId, player, player2);
          response.redirect(WebServer.GAME_URL + "?gameID=" + gameId); //Redirects player to game with UUID
        }
      }
      else {
        if(gameId.equals("")){ //Checks for an error and fixes it
          httpSession.attribute(GetHomeRoute.MESSAGE, "Not in a game, player should not be playing.");
          player.setPlaying(false);
          response.redirect(WebServer.HOME_URL);
          return null;
        }
      }


      vm.put(VIEWMODE_PARAM, mode.PLAY);

      GameBoard board = gameCenter.getGame(gameId); //gets board via gameID
      board.isWhitePlayerBoard(!board.isRedPlayer(player));

      vm.put(REDPLAYER_PARAM, board.getRedPlayer());
      vm.put(WHITEPLAYER_PARAM, board.getWhitePlayer());
      vm.put(BOARD_PARAM, board);
      vm.put(ACTIVECOLOR_PARAM,board.getPlayerColor(board.getPlayerTurn()));
      if(board.isGameOver()){ // checks to see if the game is over
        modeOptions.put("isGameOver",board.isGameOver()); //sets the game over value into the map
        modeOptions.put("gameOverMessage",board.getGameOverMessage()); //stores the gameOver message into the map
        vm.put(MODEOPTIONS_PARAM, gson.toJson(modeOptions)); //converts the modeOptions map into a json
        board.getRedPlayer().setPlaying(false); //sets the red player to not be playing
        board.getWhitePlayer().setPlaying(false); //sets the white player to not be playing
        gameCenter.addGameSave(gameId);
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

