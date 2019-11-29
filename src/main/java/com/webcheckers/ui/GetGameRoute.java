package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameBoard;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Player;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

import static spark.Spark.halt;

/**
 * The UI Controller to GET the Game page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 * @author Nicholas Curl
 */
public class GetGameRoute implements Route {
  /**
   * The logger of this class
   */
  private static final Logger LOG = Logger.getLogger(GetGameRoute.class.getName());

  /**
   * The parameter pattern for a player
   */
  static final String PLAYER_PARAM = "player";

  /**
   * The parameter pattern for the gameID
   */
  static final String GAMEID_PARAM = "gameID";

  /**
   * The parameter pattern for the redPlayer
   */
  static final String REDPLAYER_PARAM = "redPlayer";

  /**
   * The parameter pattern for the whitePlayer
   */
  static final String WHITEPLAYER_PARAM = "whitePlayer";

  /**
   * The parameter pattern for the viewMode
   */
  static final String VIEWMODE_PARAM = "viewMode";

  /**
   * The parameter pattern for the board
   */
  static final String BOARD_PARAM = "board";

  /**
   * The parameter pattern for the mode options
   */
  static final String MODEOPTIONS_PARAM = "modeOptionsAsJSON";

  /**
   * The parameter pattern for the active color
   */
  static final String ACTIVECOLOR_PARAM = "activeColor";

  /**
   * The game center from the server
   */
  private final GameCenter gameCenter;

  /**
   * The enumeration of the view mode
   */
  public enum mode {
    PLAY,
    SPECTATOR,
    REPLAY
  }

  /**
   * The template engine from the server
   */
  private final TemplateEngine templateEngine;

  /**
   * The Gson instance from the server
   */
  private final Gson gson;


  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /game} HTTP requests.
   *
   * @param gameCenter The instance of the GameCenter
   * @param templateEngine The HTML template rendering engine
   * @param gson The instance of Gson
   */
  public GetGameRoute(final GameCenter gameCenter, final TemplateEngine templateEngine, final Gson gson) {
    this.gameCenter = gameCenter;
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    this.gson = gson;
    //
    LOG.config("GetGameRoute is initialized.");
  }



  /**
   * Render the WebCheckers Game page.
   *
   * @param request The HTTP request
   * @param response The HTTP response
   * @return The rendered HTML for the Game page
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

        if (!player.isPlaying()) { // checks to see if player2 is already playing a game
          Player player2 = gameCenter.getPlayer(request.queryParams(PLAYER_PARAM));
          if (player2.isPlaying()) {
            httpSession.attribute(GetHomeRoute.MESSAGE, "Player already in game. Click a different player to begin a game of checkers.");
            response.redirect(WebServer.HOME_URL);
            return null;
          }
          if(player2.isReplaying()){ //checks to see if player 2 is replaying a game
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

