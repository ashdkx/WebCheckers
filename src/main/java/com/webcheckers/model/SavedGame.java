package com.webcheckers.model;


import com.webcheckers.appl.GameBoard;

import java.util.ArrayList;
import java.util.List;

public class SavedGame {


    private List<MoveSave> savedGame;
    private int turnNumber;


    public SavedGame(List<MoveSave> moves){
        this.savedGame = new ArrayList<>(moves);
        this.turnNumber = 0;
    }

    public List<MoveSave> getSavedGameMoves() {
        return savedGame;
    }

    public MoveSave getMove(){
        return savedGame.get(turnNumber);
    }

    public boolean hasNext(){
        return turnNumber != savedGame.size() - 1;
    }

    public boolean hasPrevious(){
        return turnNumber != 0;
    }

    public void nextTurn(){
        turnNumber++;
    }

    public void previousTurn(){
        turnNumber--;
    }

}
