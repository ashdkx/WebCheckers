package com.webcheckers.model;

/**
 * @author Nicholas Curl
 */
public class Piece {


    public enum type{
        SINGLE,
        KING
    }

    public enum color{
        RED,
        WHITE
    }

   private Piece.type pieceType;
   private Piece.color pieceColor;


   public static final Piece whiteSingle = new Piece(type.SINGLE, color.WHITE);
   public static final Piece whiteKing = new Piece(type.KING,color.WHITE);
   public static final Piece redSingle = new Piece(type.SINGLE,color.RED);
   public static final Piece redKing = new Piece(type.KING,color.RED);


    private Piece(Piece.type type, Piece.color color){
        this.pieceType = type;
        this.pieceColor = color;

    }

    public Piece.type getType(){
        return pieceType;
    }

    public Piece.color getColor() {
        return pieceColor;
    }
}
