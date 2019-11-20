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
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 * @author Ash Nguyen
 * @author Nicholas Curl
 */
public class GetHomeRoute implements Route {
   static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

   static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");
   static final Message OTHER_PLAYERS_MSG = Message.info("Click on one of these players to begin a game of checkers.");
  static final String MESSAGE_ATTR = "message";
  static final String MESSAGE_TYPE_ATTR = "messageType";
  static final String ERROR_TYPE = "error";
  static final String MESSAGE = "messageValue";
  static final String TITLE_ATTR = "title";
  static final String TITLE = "Home";
  private final GameCenter gameCenter;
  static final String CURRENT_USER_ATTR = "currentUser";
  private final String ACTIVE_PLAYERS = "activePlayers";
  static final String CURRENT_PLAYER = "currentPlayer";

  private final TemplateEngine templateEngine;

  public GetHomeRoute(final GameCenter gameCenter, final TemplateEngine templateEngine) {
    this.gameCenter = gameCenter;
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    //
    LOG.config("GetHomeRoute is initialized.");
  }


  @Override
  public Object handle(Request request, Response response) {
    final Session httpSession = request.session();
    LOG.finer("GetHomeRoute is invoked.");
    //
    Map<String, Object> vm = new HashMap<>();
    vm.put(TITLE_ATTR, TITLE);
    // display a user message in the Home page

    // show active players

    if(httpSession.attribute(CURRENT_PLAYER) != null){
      final Player player = httpSession.attribute(CURRENT_PLAYER);

      if (player.isPlaying()){
        String gameID = "";
        for (String keys : gameCenter.getGames().keySet()){ //Go through all the gameIDs and checks if player is in the game
          if((player.equals(gameCenter.getGame(keys).getRedPlayer())||player.equals(gameCenter.getGame(keys).getWhitePlayer()))&&!gameCenter.getGame(keys).isGameOver()){ //sets the gameID when its not a completed game as well
            gameID = keys;
            break;
          }
        }
        response.redirect(WebServer.GAME_URL+"?gameID="+gameID); //redirects other player to game with gameID
        halt();
        return null;
      }
      else {
        if(player.isReplaying()){ //set player replaying a game to false
          player.setReplaying(false);
        }
        vm.remove(MESSAGE_ATTR, WELCOME_MSG);
        if(httpSession.attribute(MESSAGE)!=null){
          final String message = httpSession.attribute(MESSAGE);
          vm.put(MESSAGE_ATTR,Message.error(message));
          httpSession.attribute(MESSAGE, null);
        }
        else{
          vm.put(MESSAGE_ATTR, OTHER_PLAYERS_MSG);
        }


        // print out the list of players
        vm.put(ACTIVE_PLAYERS, gameCenter.getPlayers());
        // remove current user from the list
        vm.put(CURRENT_USER_ATTR, player);
      }
    }
    else{
        vm.put(MESSAGE_ATTR, WELCOME_MSG);
        vm.put("numPlayers", gameCenter.getPlayers().size());
    }


    // render the View
    return templateEngine.render(new ModelAndView(vm , "home.ftl"));
  }
}
