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
class PostSubmitTurnRouteTest {

    private PostSubmitTurnRoute Cut;

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

        //creating and adding the game with gameID into the game center
        String gameID = UUID.randomUUID().toString();
        gameCenter.addNewGame(gameID, player1, player2);
        when(request.queryParams(GetGameRoute.GAMEID_PARAM)).thenReturn(gameID);

        Cut = new PostSubmitTurnRoute(gameCenter, gson);
    }

    @Test
    public void moveSinglePLayer1() {
        playerBoard = gameBoard.getPlayerBoard(player1);
        gameBoard.setPlayerTurn(player1);
        gameBoard.setActivePiece(gameBoard.getPiece(playerBoard, 5, 2));
        gameBoard.setActivePieceStart(new Position(5, 2));
        gameBoard.addActivePieceEnd(new Position(4,3));

        when(request.session().attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(player1);

        Cut.handle(request, response);
        assertNull(gameBoard.getPiece(playerBoard, 5, 2));
        assertNotNull(gameBoard.getPiece(playerBoard,4, 3));
    }

    @Test
    public void moveSinglePlayer2() {
        playerBoard = gameBoard.getPlayerBoard(player2);
        gameBoard.setPlayerTurn(player2);
        gameBoard.setActivePiece(gameBoard.getPiece(playerBoard, 2, 3));
        gameBoard.setActivePieceStart(new Position(2, 3));
        gameBoard.addActivePieceEnd(new Position(3,4));

        when(request.session().attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(player2);

        Cut.handle(request, response);

        assertNull(gameBoard.getPiece(playerBoard, 2, 3));
        assertNotNull(gameBoard.getPiece(playerBoard,3, 4));
    }

    @Test
    public void omniMovement() {
        playerBoard = gameBoard.getPlayerBoard(player1);
        gameBoard.setPlayerTurn(player1);

        gameBoard.setPiece(playerBoard, 3, 2, Piece.redKing);
        gameBoard.setPiece(playerBoard, 5, 4, null);

        gameBoard.setActivePiece(gameBoard.getPiece(playerBoard, 3, 2));
        gameBoard.setActivePieceStart(new Position(3,2 ));
        gameBoard.addActivePieceEnd(new Position(4,3 ));

        when(request.session().attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(player1);

        Cut.handle(request, response);

        assertNull(gameBoard.getPiece(playerBoard, 3,2 ));
        assertNull(gameBoard.getPiece(playerBoard, 5, 4));
        assertNotNull(gameBoard.getPiece(playerBoard, 4, 3));
    }

    @Test
    public void crowning() {
        playerBoard = gameBoard.getPlayerBoard(player1);
        gameBoard.setPlayerTurn(player1);

        gameBoard.setPiece(playerBoard, 1, 4, Piece.redSingle);
        gameBoard.setPiece(playerBoard, 0, 5, null);

        gameBoard.setActivePiece(gameBoard.getPiece(playerBoard, 1, 4));
        gameBoard.setActivePieceStart(new Position(1, 4));
        gameBoard.addActivePieceEnd(new Position(0,5));

        when(request.session().attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(player1);

        Cut.handle(request, response);

        assertNull(gameBoard.getPiece(playerBoard, 1, 4));
        assertNotNull(gameBoard.getPiece(playerBoard, 0, 5));
        assertEquals(gameBoard.getPiece(playerBoard, 0, 5).getType(), Piece.type.KING);
    }

    @Test
    public void jump() {
        playerBoard = gameBoard.getPlayerBoard(player1);
        gameBoard.setPlayerTurn(player1);

        int[] remove = {4, 3};

        gameBoard.setPiece(playerBoard,2, 5, null);
        gameBoard.setPiece(playerBoard, 4, 3, Piece.whiteSingle);
        gameBoard.addPieceRemove(remove);

        gameBoard.updateWhitePlayer();

        gameBoard.setActivePiece(gameBoard.getPiece(playerBoard, 5, 2));
        gameBoard.setActivePieceStart(new Position(5, 2));
        gameBoard.addActivePieceEnd(new Position(3,4));

        when(request.session().attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(player1);

        Cut.handle(request, response);

        assertNull(gameBoard.getPiece(playerBoard, 4, 3));
        assertNull(gameBoard.getPiece(playerBoard, 5, 2));
        assertNotNull(gameBoard.getPiece(playerBoard,3, 4));
    }

    @Test
    public void multipleJump() {
        playerBoard = gameBoard.getPlayerBoard(player1);
        gameBoard.setPlayerTurn(player1);

        int[] remove = {4, 3};
        int[] remove2 = {2, 5};

        gameBoard.setPiece(playerBoard, 1,6, null);
        gameBoard.setPiece(playerBoard, 4, 3, Piece.whiteSingle);

        gameBoard.addPieceRemove(remove);
        gameBoard.addPieceRemove(remove2);

        gameBoard.updateWhitePlayer();

        gameBoard.setActivePiece(gameBoard.getPiece(playerBoard, 5, 2));
        gameBoard.setActivePieceStart(new Position(5, 2));
        gameBoard.addActivePieceEnd(new Position(1,6));

        when(request.session().attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(player1);

        Cut.handle(request, response);

        assertNull(gameBoard.getPiece(playerBoard, 5, 2));
        assertNull(gameBoard.getPiece(playerBoard, 4, 3));
        assertNull(gameBoard.getPiece(playerBoard,2, 5));
        assertNotNull(gameBoard.getPiece(playerBoard, 1, 6));
    }
}