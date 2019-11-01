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
        player1.setRedPlayer(true);
        player1.setPlaying(true);
        player1.setColor(GameBoard.color.RED);
        player1.setGame(gameBoard);

        player2 = gameCenter.getPlayer(p2);
        player2.setPlaying(true);
        player2.setColor(GameBoard.color.WHITE);
        player2.setGame(gameBoard);

        gameView = new GameView(player1, player2);

        Cut = new PostSubmitTurnRoute();
    }

    @Test
    public void moveSinglePLayer1() {
        playerBoard = gameBoard.getRedPlayerBoard();
        player1.setMyTurn(true);
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
        playerBoard = gameBoard.getWhitePlayerBoard();
        player2.setMyTurn(true);
        gameBoard.setActivePiece(gameBoard.getPiece(playerBoard, 2, 3));
        gameBoard.setActivePieceStart(new Position(2, 3));
        gameBoard.addActivePieceEnd(new Position(3,4));

        when(request.session().attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(player2);

        Cut.handle(request, response);

        assertNull(gameBoard.getPiece(playerBoard, 2, 3));
        assertNotNull(gameBoard.getPiece(playerBoard,3, 4));
    }

    @Test
    public void jump() {
        playerBoard = gameBoard.getRedPlayerBoard();
        player1.setMyTurn(true);

        gameBoard.setPiece(playerBoard, 5, 2, null);
        gameBoard.setPiece(playerBoard, 4, 3, new Piece(Piece.type.SINGLE, Piece.color.RED));

        gameBoard.setPiece(playerBoard, 2, 5, null);
        gameBoard.setPiece(playerBoard, 3, 4, new Piece(Piece.type.SINGLE, Piece.color.WHITE));

        gameBoard.updateWhitePlayer();

        gameBoard.setActivePiece(gameBoard.getPiece(playerBoard, 4, 3));
        gameBoard.setActivePieceStart(new Position(4, 3));
        gameBoard.addActivePieceEnd(new Position(2,5));

        when(request.session().attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(player1);

        Cut.handle(request, response);

        //TODO: not handling jump
        assertNull(gameBoard.getPiece(playerBoard, 4, 3));
        assertNull(gameBoard.getPiece(playerBoard, 3, 4));
        assertNotNull(gameBoard.getPiece(playerBoard,2, 5));
    }
}