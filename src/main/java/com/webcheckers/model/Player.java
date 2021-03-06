package com.webcheckers.model;


/**
 * The representation of the player
 *
 * @author Nicholas Curl
 */
public class Player {

    /**
     * The username of the player
     */
    private String name;

    /**
     * Is the player in a game
     */
    private boolean playing = false;

    /**
     * Is the player replaying a game
     */
    private boolean replaying = false;

    /**
     * Is the player spectating a game
     */
    private boolean spectating = false;

    /**
     *  the player's local count o the turns seen
     */
    private int spectatorCount;

    /**
     * The representation of the player
     *
     * @param name Username of the player
     */
    public Player(String name) {
        this.name = name;
    }

    /**
     * Gets the username of the player
     *
     * @return The username of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the player to be playing
     *
     * @param status True if the player is playing a game, false otherwise
     */
    public void setPlaying(boolean status) {
        playing = status;
    }

    /**
     * Is the player playing a game
     *
     * @return Playing
     */
    public boolean isPlaying() {
        return playing;
    }

    /**
     * Sets the player be replaying
     *
     * @param replaying True if the player is replaying a game, false otherwise
     */
    public void setReplaying(boolean replaying) {
        this.replaying = replaying;
    }

    /**
     * Is the player replaying a game
     *
     * @return Replaying
     */
    public boolean isReplaying() {
        return replaying;
    }

    /**
     * Checks to see if this player equals another player
     *
     * @param player The player to compare to
     * @return True if the same, false otherwise
     */
    public boolean equals(Player player) {
        return this.name.equals(player.name);
    }

    /**
     * Print out the username when called
     *
     * @return Print username
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
    /**
     * Sets the player be spectating
     * @param spectating true if the player is spectating a game, false otherwise
     */
    public void setSpectating(boolean spectating) {
        this.spectating = spectating;
    }

    /**
     * Is the player spectating a game
     * @return replaying
     */
    public boolean isSpectating() {
        return spectating;
    }

}