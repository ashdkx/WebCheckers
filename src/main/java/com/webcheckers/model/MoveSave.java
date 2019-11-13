package com.webcheckers.model;

import com.webcheckers.appl.GameBoard;

import java.util.List;

public class MoveSave {

    private Piece[][] positions = new Piece[8][8];
    private GameBoard.color activeColor;
    private String gameOverMessage;

    public MoveSave(List<Row> redPlayerBoard, GameBoard.color activeColor, String gameOverMessage){
        this.activeColor = activeColor;
        this.gameOverMessage = gameOverMessage;
        for(int i = 0; i<8; i++){
            for(int j=0; j<8; j++){
                positions[i][j] = redPlayerBoard.get(i).getSpace(j).getPiece();
            }
        }
    }


    public Piece[][] getPositions() {
        return positions;
    }

    public GameBoard.color getActiveColor() {
        return activeColor;
    }

    public void setGameOverMessage(String gameOverMessage) {
        this.gameOverMessage = gameOverMessage;
    }
}
