package com.webcheckers.model;


import com.webcheckers.appl.GameBoard;

import java.util.ArrayList;
import java.util.List;

public class SavedGame {


    private ArrayList<MoveSave> savedGame;
    private int turnNumber;
    private GameBoard gameBoard;
    private Player playerWatching;

    public SavedGame(ArrayList<MoveSave> moves, Player redPlayer, Player whitePlayer){
        this.savedGame = new ArrayList<>(moves);
        this.turnNumber = 0;
        this.gameBoard = new GameBoard(redPlayer, whitePlayer);
        this.playerWatching = null;
        clearBoard();
    }

    public ArrayList<MoveSave> getSavedGameMoves() {
        return savedGame;
    }

    public MoveSave getMove(){
        return savedGame.get(turnNumber);
    }

    public boolean hasNext(){
        return turnNumber != savedGame.size() - 1;
    }

    public boolean hasPrevious(){
        return turnNumber != 0;
    }

    public void nextTurn(){
        turnNumber++;
        clearBoard();
    }

    public void previousTurn(){
        turnNumber--;
        clearBoard();
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    private void clearBoard(){
        for(int i=0;i<8;i++){
            for(int j = 0; j<8; j++){
                List<Row> playerBoard = gameBoard.getPlayerBoard(gameBoard.getRedPlayer());
                gameBoard.setPiece(playerBoard,i,j,null);
            }
        }
    }

    public void setPositions(Piece[][] positions){
        for(int i=0;i<8;i++){
            for(int j = 0; j<8; j++){
                List<Row> playerBoard = gameBoard.getPlayerBoard(gameBoard.getRedPlayer());
                Piece piece = positions[i][j];
                gameBoard.setPiece(playerBoard,i,j,piece);
            }
        }
    }

    public void setPlayerWatching(Player playerWatching) {
        this.playerWatching = playerWatching;
    }

    public Player getPlayerWatching() {
        return playerWatching;
    }

    public void resetSavedGame(){
        this.playerWatching = null;
        this.turnNumber = 0;
        clearBoard();
    }
}
