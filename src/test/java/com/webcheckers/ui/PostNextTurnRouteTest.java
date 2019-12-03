package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameBoard;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.MoveSave;
import com.webcheckers.model.SavedGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PostNextTurnRouteTest {

    private PostNextTurnRoute Cut;

    private static final String PLAYER1 = "player1";
    private static final String PLAYER2 = "player2";
    private GameCenter gameCenter;
    private String gameID;
    private GameBoard gameBoard;
    private SavedGame savedGame;

    private Request request;
    private Response response;
    private Session session;
    private Gson gson;

    @BeforeEach
    void setUp() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        gson = new Gson();

        gameCenter = new GameCenter();
        gameCenter.addPlayer(PLAYER1);
        gameCenter.addPlayer(PLAYER2);

        gameID = UUID.randomUUID().toString();
        gameCenter.addNewGame(gameID, gameCenter.getPlayer(PLAYER1), gameCenter.getPlayer(PLAYER2));

        gameBoard = gameCenter.getGame(gameID);

        when(request.session().attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(gameCenter.getPlayer(PLAYER1));
        when(request.queryParams(GetGameRoute.GAMEID_PARAM)).thenReturn(gameID);

        Cut = new PostNextTurnRoute(gameCenter, gson);
    }

    @Test
    void nextTurn() {
        gameBoard.addMove();
        gameBoard.getMoves().add(new MoveSave(gameBoard.getPlayerBoard(gameBoard.getRedPlayer()), gameBoard.getPlayerColor(gameBoard.getWhitePlayer()), ""));

        gameCenter.addGameSave(gameID);
        savedGame = gameCenter.getGameSave(gameID);
        savedGame.setTurnNumber(1);
        Cut.handle(request, response);
    }

    @Test
    void nextTurnFailed() {
        gameCenter.addGameSave(gameID);
        savedGame = gameCenter.getGameSave(gameID);
        savedGame.setTurnNumber(5);

        Cut.handle(request, response);
    }
}