package com.webcheckers.model;


/**
 * @author Nicholas Curl
 */
public class Space {


    private int cellIdx;
    private Piece piece;
    private boolean valid;

    public Space(int cellIdx, Piece piece, boolean valid){

        this.cellIdx = cellIdx;
        this.piece = piece;
        this.valid = valid;
    }


    public boolean isValid(){
        return valid&&piece==null;
    }


    public int getCellIdx() {
        return cellIdx;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece){

        this.piece = piece;

    }
}
