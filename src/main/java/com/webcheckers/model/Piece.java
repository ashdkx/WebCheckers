package com.webcheckers.model;

public class Piece {


   /* private enum type{
        SINGLE,
        KING
    }

    public enum color{
        RED,
        WHITE
    }*/

   private String type;
   private String color;

    public Piece(String type, String color){
        this.type = type;
        this.color = color;

    }

    public String getType() {
        return type;
    }

    public String getColor() {
        return color;
    }
    /* public Piece.type getType(){
        return ;
    }*/
}
