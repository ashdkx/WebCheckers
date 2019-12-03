package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameBoard;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Row;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PostResignGameRouteTest {

    private PostResignGameRoute CuT;

    private static final String PLAYER1 = "player1";
    private static final String PLAYER2 = "player2";
    private String gameID;
    private GameBoard gameBoard;
    private List<Row> playerBoard;

    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;
    private GameCenter gameCenter;
    private Gson gson;

    @BeforeEach
    public void setUp() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);

        response = mock(Response.class);
        engine = mock(TemplateEngine.class);

        gameCenter = new GameCenter();
        gson = new Gson();

        gameCenter.addPlayer(PLAYER1);
        gameCenter.addPlayer(PLAYER2);

        gameID = UUID.randomUUID().toString();
        gameCenter.addNewGame(gameID, gameCenter.getPlayer(PLAYER1), gameCenter.getPlayer(PLAYER2));
        gameBoard = gameCenter.getGame(gameID);
        playerBoard = gameBoard.getPlayerBoard(gameCenter.getPlayer(PLAYER1));
        when(request.queryParams(GetGameRoute.GAMEID_PARAM)).thenReturn(gameID);

        CuT = new PostResignGameRoute(gameCenter, gson);
    }

    @Test
    public void resign() {
        when(session.attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(gameCenter.getPlayer(PLAYER1));
        gameBoard.resign(gameCenter.getPlayer(PLAYER1));
        gameCenter.getPlayer(PLAYER1).setPlaying(false);

        CuT.handle(request, response);
    }

}