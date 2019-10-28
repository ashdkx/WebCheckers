package com.webcheckers.ui;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.*;

import static com.webcheckers.ui.GetGameRoute.PLAYER_PARAM;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * unit test for PlayerLobby class
 * @author - Alec Jackson
 */

public class GetGameRouteTester {
    private GetGameRoute CuT;

    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;
    private static final String SAMPLE_NAME = "Bob";
    private static final String SAMPLE_NAME_2 = "Steve";



    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);



    }
    /*
     * tests if default display works
     */
    @Test
    public void new_game() {
        // Arrange the test scenario: The session holds no game.
        final GameCenter center = new GameCenter();
        center.addPlayer(SAMPLE_NAME);
        center.addPlayer(SAMPLE_NAME_2);


        when(session.attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(center.getPlayer(SAMPLE_NAME));
        when(request.queryParams(PLAYER_PARAM)).thenReturn(SAMPLE_NAME_2);


        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Invoke the test (ignore the output)


        CuT = new GetGameRoute(center, engine);
        CuT.handle(request, response);


        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelAttribute("title", "Checkers");
        testHelper.assertViewModelAttribute("currentUser", center.getPlayer(SAMPLE_NAME));
        testHelper.assertViewModelAttribute("viewMode", GetGameRoute.mode.PLAY);
    }

    /*
     * tests if correct player is recognized
     */

    @Test
    public void isCorrectPlayer(){
        final GameCenter center = new GameCenter();

        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        center.addPlayer(SAMPLE_NAME);
        center.addPlayer(SAMPLE_NAME_2);
        Player player = center.getPlayer(SAMPLE_NAME);
        player.setRedPlayer(true);

        when(session.attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(center.getPlayer(SAMPLE_NAME));
        when(request.queryParams(PLAYER_PARAM)).thenReturn(SAMPLE_NAME_2);

        CuT = new GetGameRoute(center, engine);

        CuT.handle(request, response);
        testHelper.assertViewModelAttribute("redPlayer", player);
        testHelper.assertViewModelAttribute("whitePlayer", player.getGame().getWhitePlayer());
        testHelper.assertViewModelAttribute("board", player.getGame());

    }


}
