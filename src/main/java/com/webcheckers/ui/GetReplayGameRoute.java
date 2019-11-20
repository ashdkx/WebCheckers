package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameBoard;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.MoveSave;
import com.webcheckers.model.Player;
import com.webcheckers.model.SavedGame;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;

/**
 * @author Nicholas Curl
 */
public class GetReplayGameRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetReplayGameRoute.class.getName());

    static final String TITLE = "Game Replay";
    private final GameCenter gameCenter;
    private final TemplateEngine templateEngine;
    private final Gson gson;

    public GetReplayGameRoute(final GameCenter gameCenter, final TemplateEngine templateEngine, final Gson gson) {
        this.gameCenter = gameCenter;
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        this.gson = gson;
        //
        LOG.config("GetReplayGameRoute is initialized.");
    }


    @Override
    public Object handle(Request request, Response response) {
        final Session httpSession = request.session();
        LOG.finer("GetReplayGameRoute is invoked.");
        //
        Map<String, Object> vm = new HashMap<>();
        final Map<String, Object> modeOptions = new HashMap<>(4);
        vm.put(GetHomeRoute.TITLE_ATTR, TITLE);
        // display a user message in the Home page

        // show active players
        if(httpSession.attribute(GetHomeRoute.CURRENT_PLAYER) != null){
            final Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);
            final String gameID = request.queryParams(GetGameRoute.GAMEID_PARAM);
            vm.put(GetHomeRoute.CURRENT_USER_ATTR, player);
            vm.put(GetGameRoute.VIEWMODE_PARAM, GetGameRoute.mode.REPLAY);
            SavedGame savedGame = gameCenter.getGameSave(gameID); //gets the saved game
            if (savedGame.getPlayerWatching() == null){ //checks to see if there is no one watching
                savedGame.setPlayerWatching(player);
            }
            else if (!(savedGame.getPlayerWatching().equals(player))&& savedGame.getPlayerWatching() != null){ //checks to see if the player matches the player watching and the watching player is not null
                httpSession.attribute(GetHomeRoute.MESSAGE, "Player is already replaying the game. Click a different game to replay.");
                response.redirect(WebServer.REPLAYMODE_URL);
                return null;
            }

                modeOptions.put("hasNext", savedGame.hasNext()); // enables next button if there are available moves to display
                modeOptions.put("hasPrevious", savedGame.hasPrevious()); // enable the previous button if there are moves to display
                GameBoard board = savedGame.getGameBoard(); // gets the board to display
                MoveSave move = savedGame.getMove(); //get the current move
                if (!savedGame.hasNext()) { //checks to see if the current move is the last move and display the winning conditions if is at the end
                    modeOptions.put("isGameOver", true);
                    modeOptions.put("gameOverMessage", move.getGameOverMessage());
                }
                vm.put(GetGameRoute.MODEOPTIONS_PARAM, gson.toJson(modeOptions));
                board.isWhitePlayerBoard(false);
                savedGame.setPositions(move.getPositions()); //sets the positions of the pieces for the current move
                //stores the necessary value needed to display the game html file
                vm.put(GetGameRoute.BOARD_PARAM, board);
                vm.put(GetGameRoute.REDPLAYER_PARAM, board.getRedPlayer());
                vm.put(GetGameRoute.WHITEPLAYER_PARAM, board.getWhitePlayer());
                vm.put(GetGameRoute.ACTIVECOLOR_PARAM, move.getActiveColor());
        }
        else{
            final String gameID = request.queryParams(GetGameRoute.GAMEID_PARAM);
            SavedGame savedGame = gameCenter.getGameSave(gameID);
            savedGame.setPlayerWatching(null); //clears player watching if current player is reset
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }


        // render the View
        return templateEngine.render(new ModelAndView(vm , "game.ftl"));
    }
}
