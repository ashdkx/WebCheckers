package com.webcheckers.model;


import java.util.ArrayList;
import java.util.List;


/**
 * @author Nicholas Curl
 */
public class GameView {


    private Player redPlayer; // Representation of the red player
    private Player whitePlayer; // Representation of the white player
    private List<Row> board; // the list used to display the board
    private List<Row> redPlayerBoard = new ArrayList<>(); // the red player's board
    private List<Row> whitePlayerBoard = new ArrayList<>(); // the white player's board


    /**
     * Constructor for a new GameView for a GameBoard
     * @param redPlayer the player to be the red player
     * @param whitePlayer the player to be the white player
     */
    public GameView(Player redPlayer, Player whitePlayer) {
        this.redPlayer = redPlayer;
        this.whitePlayer = whitePlayer;
        this.board = new ArrayList<>();
        initBoard();
    }

    /**
     * Gets the board to be displayed
     * @return the board to be displayed
     */
    public List<Row> getBoard() {
        return board;
    }

    /**
     * Sets the board to be displayed
     * @param board the board to be displayed
     */
    public void setBoard(List<Row> board){
        this.board = board;
    }

    /**
     * Initializes both the red and white players' boards
     */
    private void initBoard(){
        initRedPlayer();
        initWhitePlayer();
    }

    /**
     * Initializes the red player's board
     */
    private void initRedPlayer(){
        boolean valid1 = false;


        /*
            0   1   2   3   4   5   6   7
        0   *   W   *   W   *   W   *   W
        1   W   *   W   *   W   *   W   *
        2   *   W   *   W   *   W   *   W
        3   *   *   *   *   *   *   *   *
        4   *   *   *   *   *   *   *   *
        5   R   *   R   *   R   *   R   *
        6   *   R   *   R   *   R   *   R
        7   R   *   R   *   R   *   R   *

        R = red single pieces
        W = white single pieces
        * = blank spaces
        */
        for(int i = 0; i < 8; i++){
            if (i<=2){
                redPlayerBoard.add(i,new Row(i,Piece.whiteSingle,valid1));
            }
            else if (i>=5){
                redPlayerBoard.add(i, new Row(i,Piece.redSingle,valid1));
            }
            else{
                redPlayerBoard.add(i, new Row(i,null,valid1));
            }
            valid1 = !valid1;
        }
    }

    /**
     * Initializes the white player's board
     */
    private void initWhitePlayer(){
        boolean valid2 = false;

        /*
            0   1   2   3   4   5   6   7
        0   *   R   *   R   *   R   *   R
        1   R   *   R   *   R   *   R   *
        2   *   R   *   R   *   R   *   R
        3   *   *   *   *   *   *   *   *
        4   *   *   *   *   *   *   *   *
        5   W   *   W   *   W   *   W   *
        6   *   W   *   W   *   W   *   W
        7   W   *   W   *   W   *   W   *

        R = red single pieces
        W = white single pieces
        * = blank spaces
        */
        for(int i = 0; i < 8; i++){
            if (i<=2){
                whitePlayerBoard.add(i,new Row(i,Piece.redSingle,valid2));
            }
            else if (i>=5){
                whitePlayerBoard.add(i, new Row(i,Piece.whiteSingle,valid2));
            }
            else{
                whitePlayerBoard.add(i, new Row(i,null,valid2));
            }
            valid2 = !valid2;
        }
    }

    /**
     * Sets the display board to the white players board when true
     * @param whiteBoard if the display board should be the white player's board
     */
    public void isWhitePlayerBoard(boolean whiteBoard){
        if(whiteBoard){

            this.board = whitePlayerBoard;
        }
        else {
            this.board = redPlayerBoard;
        }
    }

    /**
     * Updates the white player's board with the pieces from the red player board
     */
    public void updateWhitePlayer(){
        for(int i = 0; i<8;i++){
            for (int j = 0; j<8;j++){
                whitePlayerBoard.get(i).getSpace(7-j).setPiece(redPlayerBoard.get(7-i).getSpace(j).getPiece());
            }
        }
    }

    /**
     * Updates the red player's board with the pieces from the white player board
     */
    public void updateRedPlayer(){
        for(int i = 0; i<8;i++){
            for (int j = 0; j<8;j++){
                redPlayerBoard.get(i).getSpace(7-j).setPiece(whitePlayerBoard.get(7-i).getSpace(j).getPiece());
            }
        }
    }

    /**
     * Gets the red player's board
     * @return the red player's board
     */
    public List<Row> getRedPlayerBoard(){
        return redPlayerBoard;
    }

    /**
     * Gets the white player's board
     * @return the white player's board
     */
    public List<Row> getWhitePlayerBoard() {
        return whitePlayerBoard;
    }

    /**
     * Gets the red player
     * @return the red player
     */
    public Player getRedPlayer() {
        return redPlayer;
    }

    /**
     * Gets the white player
     * @return the white player
     */
    public Player getWhitePlayer() {
        return whitePlayer;
    }

    /**
     * A testing function to help with double jumps
     */
    private void testDoubleJumpSingle(){
        for(int i = 0; i<8; i++){
            for(int j=0; j<8; j++){
                redPlayerBoard.get(i).getSpace(j).setPiece(null);
            }
        }
        redPlayerBoard.get(5).getSpace(2).setPiece(Piece.redSingle);
        redPlayerBoard.get(4).getSpace(3).setPiece(Piece.whiteSingle);
        redPlayerBoard.get(2).getSpace(3).setPiece(Piece.whiteSingle);
        redPlayerBoard.get(2).getSpace(5).setPiece(Piece.whiteSingle);
        updateWhitePlayer();

    }


    /**
     * A testing function to help with game over states
     */
    private void testGameOver(){
        for(int i = 0; i<8; i++){
            for(int j=0; j<8; j++){
                redPlayerBoard.get(i).getSpace(j).setPiece(null);
            }
        }
        redPlayerBoard.get(5).getSpace(2).setPiece(Piece.redSingle);
        redPlayerBoard.get(4).getSpace(3).setPiece(Piece.whiteSingle);
        updateWhitePlayer();
    }

}