package com.webcheckers.appl;

import com.webcheckers.model.GameView;
import com.webcheckers.model.Player;
import com.webcheckers.model.Row;

import java.util.Iterator;

/**
 * @author Nicholas Curl
 */
public class GameBoard implements Iterable<Row> {

    private GameView game;


    public GameBoard(Player player1, Player player2){
        this.game = new GameView(player1,player2);
    }


    public GameView getGame() {
        return game;
    }

    public Player getPlayer1(){
        return game.getPlayer1();
    }

    public Player getPlayer2(){
        return game.getPlayer2();
    }

    public void setPlayer2Board(boolean playerBoard){
        game.setPlayer2Board(playerBoard);

    }

    @Override
    public Iterator<Row> iterator(){
        return game.getBoard().iterator();
    }
}
