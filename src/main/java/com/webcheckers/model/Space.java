package com.webcheckers.model;


/**
 * The representation of a space that piece can be placed in
 *
 * @author Nicholas Curl
 */
public class Space {

    /**
     * The id of the cell
     */
    private int cellIdx;

    /**
     * The piece in the space
     */
    private Piece piece;

    /**
     * Is the space valid
     */
    private boolean valid;

    /**
     * Representation of a space to be used in a row
     *
     * @param cellIdx The column of the space
     * @param piece   The piece to be set in the space
     * @param valid   Is the space a valid space
     */
    public Space(int cellIdx, Piece piece, boolean valid) {
        this.cellIdx = cellIdx;
        this.piece = piece;
        this.valid = valid;
    }

    /**
     * Checks to see if the space is valid
     *
     * @return True if it's a black space and there isn't a piece
     */
    public boolean isValid() {
        return valid && piece == null;
    }

    /**
     * Get the cell ID
     *
     * @return The cellId
     */
    public int getCellIdx() {
        return cellIdx;
    }

    /**
     * Get the piece in the space
     *
     * @return The piece in the space
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Sets a piece in the space
     *
     * @param piece The piece to set
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
    }
}
