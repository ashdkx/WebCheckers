package com.webcheckers.ui;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("UI-Tier")
public class GetSignInRouteTest {

    private GetSignInRoute CuT;

    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);

        CuT = new GetSignInRoute(engine);
    }

    @Test
    public void renderSignIn() {
        when(session.attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(null);
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        CuT.handle(request, response);

        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

        testHelper.assertViewModelAttribute(GetHomeRoute.TITLE_ATTR, GetSignInRoute.TITLE);

        testHelper.assertViewName(GetSignInRoute.VIEW_NAME);
    }

    @Test
    public void faultyLogIn() {
        when(session.attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(null);
    }

}
