package com.webcheckers.model;

public class Player{
    private String name;
    private String sessionID;
    // whether the player is the first or second player
    private int playerNumber = 0;

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

    public int getPlayerNumber(){
        return playerNumber;
    }

    public void setPlayerNumber(int number){
        playerNumber = number;
    }

    public boolean equals(Player player){
        if(this.name.equals(player.name)){
            return true;
        }
        return false;
    }
}