package com.webcheckers.ui;

import com.webcheckers.appl.GameBoard;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Ash Nguyen
 */
@Tag("UI-tier")
class PostValidateMoveRouteTest {

    private PostValidateMoveRoute Cut;

    private GameCenter gameCenter;
    private GameBoard gameBoard;
    private GameView gameView;
    private String p1 = "player1";
    private String p2 = "player2";
    private Player player1;
    private Player player2;
    private List<Row> playerBoard;

    private Request request;
    private Session session;
    private Response response;

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);

        gameCenter = new GameCenter();
        gameCenter.addPlayer(p1);
        gameCenter.addPlayer(p2);

        gameBoard = new GameBoard(gameCenter.getPlayer(p1), gameCenter.getPlayer(p2));

        player1 = gameCenter.getPlayer(p1);
        player1.setPlayer1(true);
        player1.setPlaying(true);
        player1.setColor(GameBoard.color.RED);
        player1.setMyTurn(true);
        player1.setGame(gameBoard);

        player2 = gameCenter.getPlayer(p2);
        player2.setPlaying(true);
        player2.setColor(GameBoard.color.WHITE);
        player2.setGame(gameBoard);

        gameView = new GameView(player1, player2);
        playerBoard = gameBoard.getPlayer1Board();

        Cut = new PostValidateMoveRoute();
    }

    @Test
    public void moveSingle() {
        String json;
        json = "{\"start\":{\"row\":5,\"cell\":2},\"end\":{\"row\":4, \"cell\":3}}";

        when(request.session().attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(player1);
        when(request.queryParams("actionData")).thenReturn(json);

        Cut.handle(request, response);

        // move twice error
        // to be changed
        json = "{\"start\":{\"row\":4,\"cell\":3},\"end\":{\"row\":3, \"cell\":4}}";
        when(request.queryParams("actionData")).thenReturn(json);
        Cut.handle(request, response);
    }

    @Test
    public void jumpSingle() {
        gameBoard.setPiece(playerBoard, 5, 2, null);
        gameBoard.setPiece(playerBoard, 4, 3, new Piece(Piece.type.SINGLE, Piece.color.RED));

        gameBoard.setPiece(playerBoard, 2, 5, null);
        gameBoard.setPiece(playerBoard, 3, 4, new Piece(Piece.type.SINGLE, Piece.color.WHITE));

        gameBoard.updatePlayer2();

        String json;
        json = "{\"start\":{\"row\":4,\"cell\":3},\"end\":{\"row\":2, \"cell\":5}}";

        when(request.session().attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(player1);
        when(request.queryParams("actionData")).thenReturn(json);

        Cut.handle(request, response);

    }

}