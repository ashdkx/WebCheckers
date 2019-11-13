package com.webcheckers.model;

import com.webcheckers.appl.GameBoard;


/**
 * @author Nicholas Curl
 */
public class Player{
    private String name;
    private String sessionID;

    // if the player is in a game
    private boolean playing = false;
    private boolean replaying = false;

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

    public boolean isPlaying(){
        return playing;
    }

    public void setReplaying(boolean replaying) {
        this.replaying = replaying;
    }

    public boolean isReplaying() {
        return replaying;
    }

    public boolean equals(Player player){
        return this.name.equals(player.name);
    }

    /**
     * Print out the username when called
     * @return print username
     */
    public String toString() {
        return this.name;
    }
}