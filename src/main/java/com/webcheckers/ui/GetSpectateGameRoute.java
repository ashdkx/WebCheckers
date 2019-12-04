package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameBoard;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.MoveSave;
import com.webcheckers.model.Player;
import com.webcheckers.model.SavedGame;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;

/**
 * @author Nicholas Curl
 */
public class GetSpectateGameRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetReplayGameRoute.class.getName());

    static final String TITLE = "Game Spectate";
    private final GameCenter gameCenter;
    private final TemplateEngine templateEngine;
    private final Gson gson;

    public GetSpectateGameRoute(final GameCenter gameCenter, final TemplateEngine templateEngine, final Gson gson) {
        this.gameCenter = gameCenter;
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        this.gson = gson;
        //
        LOG.config("GetSpectateGameRoute is initialized.");
    }


    @Override
    public Object handle(Request request, Response response) {
        final Session httpSession = request.session();
        LOG.finer("GetSpectateGameRoute is invoked.");
        //
        Map<String, Object> vm = new HashMap<>();
        vm.put(GetHomeRoute.TITLE_ATTR, TITLE);
        // display a user message in the Home page

        // show active players
        if(httpSession.attribute(GetHomeRoute.CURRENT_PLAYER) != null){
            final Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);
            final String gameID = request.queryParams(GetGameRoute.GAMEID_PARAM);
            vm.put(GetHomeRoute.CURRENT_USER_ATTR, player);
            vm.put(GetGameRoute.VIEWMODE_PARAM, GetGameRoute.mode.SPECTATOR);
            GameBoard game = gameCenter.getGame(gameID); //gets the saved game

            vm.put(GetGameRoute.ACTIVECOLOR_PARAM,game.getPlayerColor(game.getPlayerTurn()));
            game.isWhitePlayerBoard(false);
            vm.put(GetGameRoute.BOARD_PARAM, game);
            vm.put(GetGameRoute.REDPLAYER_PARAM, game.getRedPlayer());
            vm.put(GetGameRoute.WHITEPLAYER_PARAM, game.getWhitePlayer());
            if(game.isGameOver()){

                httpSession.attribute(GetHomeRoute.MESSAGE, Message.info(game.getGameOverMessage()));
                response.redirect(WebServer.HOME_URL);
                return null;
            }


        }
        else{
            final String gameID = request.queryParams(GetGameRoute.GAMEID_PARAM);
            GameBoard game = gameCenter.getCurrentGame(gameID);
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }


        // render the View
        return templateEngine.render(new ModelAndView(vm , "game.ftl"));
    }
}
