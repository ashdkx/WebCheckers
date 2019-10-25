package com.webcheckers.model;

import com.webcheckers.appl.GameBoard;


/**
 * @author Nicholas Curl
 */
public class Player{
    private String name;
    private String sessionID;

    // if the player is in a game
    private boolean playing = false;
    private boolean player1 = false;
    private GameBoard game;
    private GameBoard.color color;
    private boolean myTurn = false;
    private int totalPieces = 12;
    private boolean singleMove = false;

    public Player(String name, String sessionID) {
        this.name = name;
        this.sessionID = sessionID;
    }

    public String getName() {
        return name;
    }

    public String getSessionID(){
        return sessionID;
    }


    public void setPlaying(boolean status){
        playing = status;
    }

    public void setPlayer1(boolean status){
        player1 = status;
    }

    public boolean isPlayer1() {
        return player1;
    }

    public boolean isPlaying(){
        return playing;
    }

    public boolean equals(Player player){
        if(this.name.equals(player.name)){
            return true;
        }
        return false;
    }

    public void setGame(GameBoard game){
        this.game = game;
    }

    public GameBoard getGame() {
        return game;
    }

    /**
     * Print out the username when called
     * @return print username
     */
    public String toString() {
        return this.name;
    }

    public void setColor(GameBoard.color color){
        this.color = color;
    }

    public GameBoard.color getColor() {
        return color;
    }

    public void setMyTurn(boolean myTurn){
        this.myTurn = myTurn;
    }

    public boolean isMyTurn() {
        return myTurn;
    }

    public int getTotalPieces() {
        return totalPieces;
    }

    public void addTotalPieces(){
        totalPieces++;
    }

    public void removeTotalPieces(int amount){
        totalPieces-=amount;
    }

    public boolean isNotActiveColor(Piece piece){
        boolean valid = false;
        if(piece != null) {
            switch (piece.getColor()) {
                case RED:
                    if (color != GameBoard.color.RED) {
                        valid = true;
                    }
                    break;
                case WHITE:
                    if (color != GameBoard.color.WHITE) {
                        valid = true;
                    }
                    break;
            }
        }
        return valid;
    }

    public boolean isSingleMove() {
        return singleMove;
    }

    public void setSingleMove(boolean move){
        singleMove = move;
    }
}