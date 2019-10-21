package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameBoard;
import com.webcheckers.model.Player;
import com.webcheckers.model.Position;
import com.webcheckers.model.Row;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

import java.util.List;
import java.util.logging.Logger;

/**
 * @author Nicholas Curl
 */

public class PostSubmitTurnRoute implements Route {

    private static final Logger LOG = Logger.getLogger(PostSubmitTurnRoute.class.getName());


    public PostSubmitTurnRoute(){
        LOG.config("PostSubmitTurnRoute is initialized.");
    }

    @Override
    public Object handle(Request request, Response response) {


        LOG.finer("PostSubmitTurnRoute is invoked.");
        Gson gson = new Gson();
        final Session httpSession = request.session();

        Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);

        GameBoard board = player.getGame();
        List<Row> playerBoard;
        if(player.isPlayer1()){
            playerBoard = player.getGame().getPlayer1Board();
        }
        else{
            playerBoard = player.getGame().getPlayer2Board();
        }

        Position moveEnd = board.getActiveEnd();
        Position moveStart = board.getActiveStart();

        playerBoard.get(moveStart.getRow()).getSpace(moveStart.getCell()).setPiece(null);
        playerBoard.get(moveEnd.getRow()).getSpace(moveEnd.getCell()).setPiece(board.getActivePiece());
        board.setActivePiece(null);

        String json= "";
        json = gson.toJson(Message.info("Valid Move"));

        if(player.isPlayer1()){
            board.updatePlayer2();
            player.setMyTurn(false);
            board.getPlayer2().setMyTurn(true);
        }
        else{
            board.updatePlayer1();
            player.setMyTurn(false);
            board.getPlayer1().setMyTurn(true);
        }
        return json;
    }
}
