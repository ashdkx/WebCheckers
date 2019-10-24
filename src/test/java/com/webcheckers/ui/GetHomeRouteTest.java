package com.webcheckers.ui;

import com.webcheckers.appl.GameCenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class GetHomeRouteTest {
    private GetHomeRoute CuT;
    private Session session;
    private Request request;
    private TemplateEngine engine;
    private Response response;
    private GameCenter gameCenter;

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        gameCenter = new GameCenter();
        CuT = new GetHomeRoute(gameCenter, engine);
    }

    @Test
    public void new_session() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Invoke the test
        CuT.handle(request, response);

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute(GetHomeRoute.MESSAGE_ATTR, GetHomeRoute.WELCOME_MSG);
        //testHelper.assertViewModelAttribute(GetHomeRoute.MESSAGE_ATTR, GameCenter.NO_GAMES_MESSAGE);
        testHelper.assertViewModelAttribute(GetHomeRoute.CURRENT_PLAYER, null);
        //   * test view name
        String viewName = "home.ftl";
        testHelper.assertViewName(viewName);
        //   * verify that a player service object and the session timeout watchdog are stored
        //   * in the session.
        //verify(session).attribute(eq(GetHomeRoute.PLAYERSERVICES_KEY), any(PlayerServices.class));
        //verify(session).attribute(eq(GetHomeRoute.TIMEOUT_SESSION_KEY),
               // any(SessionTimeoutWatchdog.class));
    }
}