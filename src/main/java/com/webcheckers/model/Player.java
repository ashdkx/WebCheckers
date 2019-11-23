package com.webcheckers.model;

/**
 * @author Nicholas Curl
 */
public class Player{
    private String name;

    // if the player is in a game
    private boolean playing = false;
    private boolean replaying = false; // if the player is replaying a game

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public void setPlaying(boolean status){
        playing = status;
    }

    public boolean isPlaying(){
        return playing;
    }

    /**
     * Sets the player be replaying
     * @param replaying true if the player is replaying a game, false otherwise
     */
    public void setReplaying(boolean replaying) {
        this.replaying = replaying;
    }

    /**
     * Is the player replaying a game
     * @return replaying
     */
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