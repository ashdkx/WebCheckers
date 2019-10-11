package com.webcheckers.model;

public class Space {


    private int cellIdx;
    private Piece piece;
    private boolean valid;

    public Space(int cellIdx,Piece piece, boolean valid){

        this.cellIdx = cellIdx;
        this.piece = piece;
        this.valid = valid;
    }


    public boolean isValid(){
        return valid&&(getPiece()==null);
    }


    public int getCellIdx() {
        return cellIdx;
    }

    public Piece getPiece() {
        return piece;
    }
}
