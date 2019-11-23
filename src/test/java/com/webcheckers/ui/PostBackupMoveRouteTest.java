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

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Ash Nguyen
 */
@Tag("UI-tier")
class PostBackupMoveRouteTest {
    private PostBackupMoveRoute Cut;

    private GameCenter gameCenter;
    private GameBoard gameBoard;
    private GameView gameView;
    private String p1 = "player1";
    private String p2 = "player2";
    private Player player1;
    private Player player2;

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

        Cut = new PostBackupMoveRoute(gameCenter, gson);
    }

    @Test
    public void backupSuccess() {
        Piece piece = new Piece(Piece.type.SINGLE, Piece.color.RED);

        gameBoard.setActivePiece(piece);
        gameBoard.setActivePieceMoves(1);
        gameBoard.setActivePieceStart(new Position(5, 2));
        gameBoard.addActivePieceEnd(new Position(4, 3));


        when(request.session().attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(player1);

        Cut.handle(request, response);
    }

    @Test
    public void backupFailure() {

        gameBoard.setActivePieceStart(new Position(5, 2));
        gameBoard.addActivePieceEnd(new Position(6, 1));


        when(request.session().attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(player1);

        Cut.handle(request, response);
    }

}