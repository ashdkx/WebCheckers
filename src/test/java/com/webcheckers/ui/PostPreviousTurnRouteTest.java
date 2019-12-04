package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameBoard;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.SavedGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PostPreviousTurnRouteTest {

    private PostPreviousTurnRoute CuT;

    private static final String PLAYER1 = "player1";
    private static final String PLAYER2 = "player2";
    private String gameID;
    private GameBoard gameBoard;
    private SavedGame savedGame;

    private Request request;
    private Session session;
    private Response response;
    private GameCenter gameCenter;
    private Gson gson;

    @BeforeEach
    public void setUp() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);

        response = mock(Response.class);

        gameCenter = new GameCenter();
        gson = new Gson();

        gameCenter.addPlayer(PLAYER1);
        gameCenter.addPlayer(PLAYER2);


        gameID = UUID.randomUUID().toString();
        gameCenter.addNewGame(gameID, gameCenter.getPlayer(PLAYER1), gameCenter.getPlayer(PLAYER2));
        gameBoard = gameCenter.getGame(gameID);
        when(request.queryParams(GetGameRoute.GAMEID_PARAM)).thenReturn(gameID);

        CuT = new PostPreviousTurnRoute(gameCenter, gson);
    }

    @Test
    public void prevTurn() {
        //TODO: IMPLEMENT THIS
    }
}