package com.webcheckers.model;

import com.webcheckers.appl.GameBoard;

public class Player{
    private String name;
    private String sessionID;

    // if the player is in a game
    private boolean playing = false;
    private boolean player1 = false;
    private GameBoard game;

    public Player(String name, String sessionID) {
        this.name = name;
        this.sessionID = sessionID;
    }

    public String getName() {
        return name;
    }

    public String getSessionID(){
        return sessionID;
    }


    public void setPlaying(boolean status){
        playing = status;
    }

    public void setPlayer1(boolean status){
        player1 = status;
    }

    public boolean isPlayer1() {
        return player1;
    }

    public boolean isPlaying(){
        return playing;
    }

    public boolean equals(Player player){
        if(this.name.equals(player.name)){
            return true;
        }
        return false;
    }

    public void setGame(GameBoard game){
        this.game = game;
    }

    public GameBoard getGame() {
        return game;
    }

    /**
     * Print out the username when called
     * @return print username
     */
    public String toString() {
        return this.name;
    }
}