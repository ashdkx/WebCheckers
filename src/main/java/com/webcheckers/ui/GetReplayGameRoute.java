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

public class GetReplayGameRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetReplayGameRoute.class.getName());

    static final String TITLE = "Game Replay";
    private final GameCenter gameCenter;
    private final TemplateEngine templateEngine;
    private final Gson gson;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public GetReplayGameRoute(final GameCenter gameCenter, final TemplateEngine templateEngine, final Gson gson) {
        this.gameCenter = gameCenter;
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        this.gson = gson;
        //
        LOG.config("GetReplayGameRoute is initialized.");
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
            SavedGame savedGame = gameCenter.getGameSave(gameID);
            if (savedGame.getPlayerWatching() == null){
                savedGame.setPlayerWatching(player);
            }
            else if (!(savedGame.getPlayerWatching().equals(player))&& savedGame.getPlayerWatching() != null){
                httpSession.attribute(GetHomeRoute.MESSAGE, "Player is already replaying the game. Click a different game to replay.");
                response.redirect(WebServer.REPLAYMODE_URL);
                return null;
            }

                modeOptions.put("hasNext", savedGame.hasNext());
                modeOptions.put("hasPrevious", savedGame.hasPrevious());
                GameBoard board = savedGame.getGameBoard();
                MoveSave move = savedGame.getMove();
                if (!savedGame.hasNext()) {
                    modeOptions.put("isGameOver", true);
                    modeOptions.put("gameOverMessage", move.getGameOverMessage());
                }
                vm.put(GetGameRoute.MODEOPTIONS_PARAM, gson.toJson(modeOptions));
                board.isWhitePlayerBoard(false);
                savedGame.setPositions(move.getPositions());
                vm.put(GetGameRoute.BOARD_PARAM, board);
                vm.put(GetGameRoute.REDPLAYER_PARAM, board.getRedPlayer());
                vm.put(GetGameRoute.WHITEPLAYER_PARAM, board.getWhitePlayer());
                vm.put(GetGameRoute.ACTIVECOLOR_PARAM, move.getActiveColor());
        }
        else{
            final String gameID = request.queryParams(GetGameRoute.GAMEID_PARAM);
            SavedGame savedGame = gameCenter.getGameSave(gameID);
            savedGame.setPlayerWatching(null);
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }


        // render the View
        return templateEngine.render(new ModelAndView(vm , "game.ftl"));
    }
}
