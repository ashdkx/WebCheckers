package com.webcheckers.ui;

import com.google.gson.Gson;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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
    private Gson gson;
    private String gameID;

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        gson = new Gson();

        gameCenter = new GameCenter();
        gameCenter.addPlayer(p1);
        gameCenter.addPlayer(p2);

        gameBoard = new GameBoard(gameCenter.getPlayer(p1), gameCenter.getPlayer(p2));

        player1 = gameCenter.getPlayer(p1);
        player1.setPlaying(true);

        player2 = gameCenter.getPlayer(p2);
        player2.setPlaying(true);

        gameView = new GameView(player1, player2);
        playerBoard = gameBoard.getPlayerBoard(player1);
        /*
        for(int i = 0; i<8; i++){
            for(int j=0; j<8; j++){
                playerBoard.get(i).getSpace(j).setPiece(null);
            }
        }

         */

        //creating and adding the game with gameID into the game center
        gameID = UUID.randomUUID().toString();
        gameCenter.addNewGame(gameID, player1, player2);
        gameBoard = gameCenter.getGame(gameID);
        when(request.queryParams(GetGameRoute.GAMEID_PARAM)).thenReturn(gameID);

        Cut = new PostValidateMoveRoute(gameCenter, gson);
    }

    @Test
    public void moveSingle() {
        gameBoard.setPiece(playerBoard, 5, 2, Piece.redSingle);
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
        gameBoard.setPiece(playerBoard, 5, 2, Piece.redSingle);
        //gameBoard.setPiece(playerBoard, 5, 4, Piece.redSingle);
        gameBoard.setPiece(playerBoard, 4, 3, Piece.whiteSingle);
        //gameBoard.setPiece(playerBoard, 4, 1, Piece.whiteSingle);


        gameBoard.updateWhitePlayer();

        String json;
        json = "{\"start\":{\"row\":5,\"cell\":2},\"end\":{\"row\":3, \"cell\":4}}";

        when(request.session().attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(player1);
        when(request.queryParams("actionData")).thenReturn(json);

        Cut.handle(request, response);

        // move after jump error
        json = "{\"start\":{\"row\":3,\"cell\":4},\"end\":{\"row\":2, \"cell\":5}}";
        when(request.queryParams("actionData")).thenReturn(json);
        Cut.handle(request, response);

    }

    @Test
    public void jumpMultiple() {
        playerBoard.get(4).getSpace(3).setPiece(Piece.redSingle);
        playerBoard.get(3).getSpace(4).setPiece(Piece.whiteSingle);
        playerBoard.get(1).getSpace(4).setPiece(Piece.whiteSingle);
        playerBoard.get(1).getSpace(6).setPiece(Piece.whiteSingle);
        gameBoard.updateWhitePlayer();

        String json;
        json = "{\"start\":{\"row\":4,\"cell\":3},\"end\":{\"row\":2, \"cell\":5}}";

        when(request.session().attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(player1);
        when(request.queryParams("actionData")).thenReturn(json);

        Cut.handle(request, response);

        json = "{\"start\":{\"row\":2,\"cell\":5},\"end\":{\"row\":0, \"cell\":7}}";

        Cut.handle(request, response);
    }

    @Test
    public void omniMovement() {
        //playerBoard.get(4).getSpace(3).setPiece(Piece.redKing);
        gameBoard.setPiece(playerBoard, 4,3,Piece.redKing);
        gameBoard.updateWhitePlayer();

        String json;
        json = "{\"start\":{\"row\":4,\"cell\":3},\"end\":{\"row\":4, \"cell\":5}}";

        when(request.session().attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(player1);
        when(request.queryParams("actionData")).thenReturn(json);

        Cut.handle(request, response);
    }

    @Test
    public void jumpGhostError() {
        String json;
        gameBoard.setPiece(playerBoard, 5, 2, Piece.redSingle);
        json = "{\"start\":{\"row\":5,\"cell\":2},\"end\":{\"row\":3, \"cell\":4}}";

        when(request.session().attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(player1);
        when(request.queryParams("actionData")).thenReturn(json);

        Cut.handle(request, response);
    }

    @Test
    public void jumpOwnError() {
        String json;
        gameBoard.setPiece(playerBoard, 6, 1, Piece.redSingle);
        gameBoard.setPiece(playerBoard, 5, 2, Piece.redSingle);
        json = "{\"start\":{\"row\":6,\"cell\":1},\"end\":{\"row\":4, \"cell\":3}}";

        when(request.session().attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(player1);
        when(request.queryParams("actionData")).thenReturn(json);

        Cut.handle(request, response);
    }
}