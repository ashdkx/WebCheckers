package com.webcheckers.model;

/**
 * @author Nicholas Curl
 */
public class Player{
    private String name;

    // if the player is in a game
    private boolean playing = false;
    private boolean replaying = false; // if the player is replaying a game
    private boolean spectating = false; // if the player is spectating
    private int spectatorCount = 0; //count for determine turn in spectator mode

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
     * Sets the player be spectating
     * @param spectating true if the player is spectating a game, false otherwise
     */
    public void setSpectating(boolean spectating) {
        this.spectating = spectating;
    }


    /**
     * Is the player replaying a game
     * @return replaying
     */
    public boolean isReplaying() {
        return replaying;
    }

    /**
     * Is the player spectating a game
     * @return replaying
     */
    public boolean isSpectating() {
        return spectating;
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

    /**
     * returns counter for spectator mode
     * @return spectator count
     */
    public int getSpectatorCount(){return spectatorCount;}

    /**
     * sets a new spectator count
     * @param newCount new given spectator count
     */
    public void setSpectatorCount(int newCount){spectatorCount = newCount;}
}