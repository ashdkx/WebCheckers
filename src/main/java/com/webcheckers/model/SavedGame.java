package com.webcheckers.model;


import com.webcheckers.appl.GameBoard;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nicholas Curl
 * Model for processing for game to be saved.
 */
public class SavedGame {


    private ArrayList<MoveSave> savedGame; //List of the moves of the game being saved
    private int turnNumber; // turn number in regards to which move
    private GameBoard gameBoard; // the board to display the replay
    private Player playerWatching; // prevents other player from watching

    public SavedGame(ArrayList<MoveSave> moves, Player redPlayer, Player whitePlayer){
        this.savedGame = new ArrayList<>(moves);
        this.turnNumber = 0;
        this.gameBoard = new GameBoard(redPlayer, whitePlayer);
        this.playerWatching = null;
        clearBoard();
    }

    /**
     * Get the list of moves
     * @return the moves list
     */
    public ArrayList<MoveSave> getSavedGameMoves() {
        return savedGame;
    }

    /**
     * Get the move at a specific turn
     * @return the move at the specific turn
     */
    public MoveSave getMove(){
        return savedGame.get(turnNumber);
    }

    /**
     * Is the replay at the end of the move list
     * @return true if it is not at the end, false otherwise
     */
    public boolean hasNext(){
        return turnNumber != savedGame.size() - 1;
    }

    /**
     * Is the replay at the start of the move list
     * @return true if it is not at the start, false otherwise
     */
    public boolean hasPrevious(){
        return turnNumber > 0;
    }

    /**
     * Increment the turn number and clear the board for the next move in the list
     */
    public void nextTurn(){
        turnNumber++;
        clearBoard();
    }

    /**
     * Decrement the turn number and clear the board for the previous move in the list
     */
    public void previousTurn(){
        turnNumber--;
        clearBoard();
    }

    /**
     * Get the turn number
     * @return the turn number
     */
    public int getTurnNumber() {
        return turnNumber;
    }


    /**
     * Set the turn number to a specified value
     * @param turnNumber the number to set the turn number
     */
    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    /**
     * Gets the game board
     * @return the game board
     */
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * Goes through the entire board and removes all the pieces
     */
    private void clearBoard(){
        for(int i=0;i<8;i++){
            for(int j = 0; j<8; j++){
                List<Row> playerBoard = gameBoard.getPlayerBoard(gameBoard.getRedPlayer());
                gameBoard.setPiece(playerBoard,i,j,null);
            }
        }
    }


    /**
     * Goes through the entire board and sets pieces at that space
     * @param positions the 2d array of pieces to be set
     */
    public void setPositions(Piece[][] positions){
        for(int i=0;i<8;i++){
            for(int j = 0; j<8; j++){
                List<Row> playerBoard = gameBoard.getPlayerBoard(gameBoard.getRedPlayer());
                Piece piece = positions[i][j];
                gameBoard.setPiece(playerBoard,i,j,piece);
            }
        }
    }

    /**
     * Set the player watching the replay to the player inputted
     * @param playerWatching the player that will be watching the replay
     */
    public void setPlayerWatching(Player playerWatching) {
        this.playerWatching = playerWatching;
    }

    /**
     * Get the player that is watching the replay
     * @return the player watching the replay
     */
    public Player getPlayerWatching() {
        return playerWatching;
    }


    /**
     * A helper function to reset the saved game back to the starting state;
     */
    public void resetSavedGame(){
        this.playerWatching = null;
        this.turnNumber = 0;
        clearBoard();
    }
}
