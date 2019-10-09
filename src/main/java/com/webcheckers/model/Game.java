package com.webcheckers.model;

public class Game {
    private String otherPlayerName;
    private int playerNumber = 0;
    private int GameBoard;

    public Game(String otherPlayerName, int playerNumber){
        this.otherPlayerName = otherPlayerName;
        this.playerNumber = playerNumber;

    }
    public int getPlayerNumber(){
        return playerNumber;
    }

    public void setPlayerNumber(int number){
        playerNumber = number;
    }
}
