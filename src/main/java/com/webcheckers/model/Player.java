package com.webcheckers.model;

public class Player{
    private String name;
    private String sessionID;

    // if the player is in a game
    private boolean ifPlaying = false;


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


    public void setIfPlaying(boolean status){
        ifPlaying = status;
    }

    public boolean getIfPlaying(){
        return ifPlaying;
    }

    public boolean equals(Player player){
        if(this.name.equals(player.name)){
            return true;
        }
        return false;
    }


    /**
     * Print out the username when called
     * @return print username
     */
    public String toString() {
        return this.name;
    }
}