package com.webcheckers.model;

import com.webcheckers.appl.GameBoard;

import java.util.List;


/**
 * @author Nicholas Curl
 *
 * Stores values of a move
 */
public class MoveSave {

    private Piece[][] positions = new Piece[8][8]; // the 2d array of the entire board
    private GameBoard.color activeColor; // the active player's color
    private String gameOverMessage; // the game over message

    /**
     * The constructor used to create a move save
     * @param redPlayerBoard the list representing the red player's board
     * @param activeColor the enum value of the active color
     * @param gameOverMessage the string of the game over message
     */
    public MoveSave(List<Row> redPlayerBoard, GameBoard.color activeColor, String gameOverMessage){
        this.activeColor = activeColor;
        this.gameOverMessage = gameOverMessage;
        for(int i = 0; i<8; i++){ // goes through the entire board and stores it into the 2d array
            for(int j=0; j<8; j++){
                positions[i][j] = redPlayerBoard.get(i).getSpace(j).getPiece();
            }
        }
    }

    /**
     * Get the 2d array of the pieces
     * @return the 2d array of the pieces
     */
    public Piece[][] getPositions() {
        return positions;
    }

    /**
     * Get the active color
     * @return the active color
     */
    public GameBoard.color getActiveColor() {
        return activeColor;
    }

    /**
     * Get the game over message
     * @return the game over message
     */
    public String getGameOverMessage() {
        return gameOverMessage;
    }
}
