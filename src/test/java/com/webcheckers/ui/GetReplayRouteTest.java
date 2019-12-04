package com.webcheckers.ui;

import com.webcheckers.appl.GameBoard;
import com.webcheckers.appl.GameCenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

class GetReplayRouteTest {

    private GetReplayRoute Cut;

    private static final String PLAYER1 = "player1";
    private static final String PLAYER2 = "player2";
    private GameCenter gameCenter;
    private String gameID;
    private GameBoard gameBoard;

    private TemplateEngine templateEngine;
    private Request request;
    private Response response;
    private Session session;

    @BeforeEach
    public void setUp() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        templateEngine = mock(TemplateEngine.class);

        gameCenter = new GameCenter();
        gameCenter.addPlayer(PLAYER1);
        gameCenter.addPlayer(PLAYER2);

        gameID = UUID.randomUUID().toString();
        gameCenter.addNewGame(gameID, gameCenter.getPlayer(PLAYER1), gameCenter.getPlayer(PLAYER2));
        gameBoard = gameCenter.getGame(gameID);

        Cut = new GetReplayRoute(gameCenter, templateEngine);
    }

    @Test
    public void spectateTrue() {
        gameCenter.addCurrentGame(gameID);
        gameCenter.addGameSave(gameID);
        when(session.attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(gameCenter.getPlayer(PLAYER1));
        when(session.attribute(GetHomeRoute.MESSAGE)).thenReturn("Replaying");
        when(request.queryParams(GetGameRoute.GAMEID_PARAM)).thenReturn(gameID);

        Cut.handle(request, response);

    }

    @Test
    public void spectateNoGame() {
        when(session.attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(gameCenter.getPlayer(PLAYER1));
        when(request.queryParams(GetGameRoute.GAMEID_PARAM)).thenReturn(gameID);

        try {
            Cut.handle(request, response);
            verify(response).redirect(WebServer.HOME_URL);
        } catch (HaltException e) {
            //expected
        }
    }

    @Test
    public void spectateNoPlayer() {
        when(session.attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(null);
        try {
            Cut.handle(request, response);
        } catch (HaltException e) {
            //expected
        }

    }
}