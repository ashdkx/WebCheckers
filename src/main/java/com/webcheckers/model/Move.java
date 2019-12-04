package com.webcheckers.model;


/**
 * Data structure for move to be used by the Gson
 *
 * @author Nicholas Curl
 */
public class Move {

    /**
     * The starting position
     */
    private Position start;

    /**
     * The ending position
     */
    private Position end;

    /**
     * The constructor for a move
     */
    public Move() {

    }

    /**
     * Get the starting position
     *
     * @return The starting position
     */
    public Position getStart() {
        return start;
    }

    /**
     * Get the ending position
     *
     * @return The ending position
     */
    public Position getEnd() {
        return end;
    }


}
