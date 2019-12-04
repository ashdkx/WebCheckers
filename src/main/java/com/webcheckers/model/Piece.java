package com.webcheckers.model;


/**
 * Creates the pieces need to play the game
 *
 * @author Nicholas Curl
 */
public class Piece {

    /**
     * The enumeration of the piece's type
     */
    public enum type {
        SINGLE,
        KING
    }

    /**
     * The enumeration of the piece's color
     */
    public enum color {
        RED,
        WHITE
    }

    /**
     * The piece's type
     */
    private Piece.type pieceType;

    /**
     * The piece's color
     */
    private Piece.color pieceColor;

    /**
     * The representation of a single piece with the color white
     */
    public static final Piece whiteSingle = new Piece(type.SINGLE, color.WHITE);

    /**
     * The representation of a king piece with the color white
     */
    public static final Piece whiteKing = new Piece(type.KING, color.WHITE);

    /**
     * The representation of a single piece with the color red
     */
    public static final Piece redSingle = new Piece(type.SINGLE, color.RED);

    /**
     * The representation of a king piece with the color red
     */
    public static final Piece redKing = new Piece(type.KING, color.RED);

    /**
     * The constructor for a piece
     *
     * @param type  The type of piece
     * @param color The color of the piece
     */
    public Piece(Piece.type type, Piece.color color) {
        this.pieceType = type;
        this.pieceColor = color;
    }

    /**
     * Get the piece's type
     *
     * @return The piece's type
     */
    public Piece.type getType() {
        return pieceType;
    }

    /**
     * Gets the color of the piece
     *
     * @return The color of the piece
     */
    public Piece.color getColor() {
        return pieceColor;
    }
}
