package com.webcheckers.ui;

import com.webcheckers.appl.GameBoard;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Player;
import com.webcheckers.model.PlayerLobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@Tag("UI-tier")
class PostSignOutRouteTest {

    private PostSignOutRoute Cut;

    private GameCenter gameCenter;

    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine templateEngine;

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        templateEngine = mock(TemplateEngine.class);
        response = mock(Response.class);

        gameCenter = new GameCenter();

        Cut = new PostSignOutRoute(gameCenter, templateEngine);


    }

    @Test
    void signedOut() {
        String name = "player";
        gameCenter.addPlayer(name);

        Player player = gameCenter.getPlayer(name);

        when(session.attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(player);

        Cut.handle(request, response);

        assertNull(gameCenter.getPlayer(name));
        assertTrue(session.attributes().isEmpty());


    }
}