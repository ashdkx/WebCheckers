package com.webcheckers.model;

import com.webcheckers.appl.GameBoard;

import java.util.List;


/**
 * Stores values of a move
 *
 * @author Nicholas Curl
 */
public class MoveSave {

    /**
     * The 2d array of the entire board
     */
    private Piece[][] positions = new Piece[8][8];

    /**
     * The active player's color
     */
    private GameBoard.color activeColor;

    /**
     * The game over message
     */
    private String gameOverMessage;

    /**
     * The constructor used to create a move save
     *
     * @param redPlayerBoard The list representing the red player's board
     * @param activeColor The enum value of the active color
     * @param gameOverMessage The string of the game over message
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
     *
     * @return The 2d array of the pieces
     */
    public Piece[][] getPositions() {
        return positions;
    }

    /**
     * Get the active color
     *
     * @return The active color
     */
    public GameBoard.color getActiveColor() {
        return activeColor;
    }

    /**
     * Get the game over message
     *
     * @return The game over message
     */
    public String getGameOverMessage() {
        return gameOverMessage;
    }

    /**
     * Set the game over message for the saved move
     *
     * @param gameOverMessage The game over message
     */
    public void setGameOverMessage(String gameOverMessage) {
        this.gameOverMessage = gameOverMessage;
    }
}
