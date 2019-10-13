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

   private Piece.type type;
   private Piece.color color;

    public Piece(Piece.type type, Piece.color color){
        this.type = type;
        this.color = color;

    }

    public Piece.type getType(){
        return type;
    }

    public Piece.color getColor() {
        return color;
    }
}
