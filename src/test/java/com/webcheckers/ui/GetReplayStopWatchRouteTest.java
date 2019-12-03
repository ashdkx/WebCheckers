package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameBoard;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.MoveSave;
import com.webcheckers.model.Row;
import com.webcheckers.model.SavedGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetReplayStopWatchRouteTest {

    private GetReplayStopWatchRoute Cut;

    private static final String PLAYER1 = "player1";
    private static final String PLAYER2 = "player2";
    private String gameID;
    private SavedGame savedGame;
    private GameBoard gameBoard;
    private List<Row> playerBoard;

    private Request request;
    private Session session;
    private Response response;
    private GameCenter gameCenter;

    @BeforeEach
    void setUp() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);

        response = mock(Response.class);

        gameCenter = new GameCenter();

        gameCenter.addPlayer(PLAYER1);
        gameCenter.addPlayer(PLAYER2);

        gameID = UUID.randomUUID().toString();
        gameCenter.addNewGame(gameID, gameCenter.getPlayer(PLAYER1), gameCenter.getPlayer(PLAYER2));
        gameBoard = gameCenter.getGame(gameID);
        playerBoard = gameBoard.getPlayerBoard(gameCenter.getPlayer(PLAYER1));

        gameCenter.addGameSave(gameID);
        savedGame =  gameCenter.getGameSave(gameID);

        //savedGame = new SavedGame(gameBoard.getMoves(), gameCenter.getPlayer(PLAYER1), gameCenter.getPlayer(PLAYER2));
        when(session.attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(gameCenter.getPlayer(PLAYER1));
        when(request.queryParams(GetGameRoute.GAMEID_PARAM)).thenReturn(gameID);

        Cut = new GetReplayStopWatchRoute(gameCenter);
    }

    @Test
    public void getStopWatchRoute() {
        gameCenter.getPlayer(PLAYER1).setReplaying(true);

        savedGame.setTurnNumber(5);
        gameBoard.addMove();
        Cut.handle(request, response);

        verify(response).redirect(WebServer.HOME_URL);
    }
}