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


    @Override
    public Iterator<Row> iterator(){
        return game.getBoard().iterator();
    }
}
