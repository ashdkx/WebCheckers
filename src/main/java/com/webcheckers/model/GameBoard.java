package com.webcheckers.model;

public class GameBoard {
    public enum squares{
        EMPTY,
        INVALID,
        PLAYER1,
        PLAYER2,
    }

    private squares[][] board;
    private Player player1;
    private Player player2;

    public GameBoard(Player player1, Player player2) {
        this.board = new squares[8][8];
        this.player1 = player1;
        this.player2 = player2;
    }

    public void initBoard(){
        for(int x = 0; x < 8; x++){
            for(int y = 0; y < 8; y++){
                if(x % 2 == 0 && y % 2 == 0){
                    this.board[x][y] = squares.INVALID;
                }
                if(x % 2 == 1 && y % 2 == 1){
                    this.board[x][y] = squares.INVALID;
                }
                if((x == 0 || x == 2) && (y % 2 == 1)){
                    this.board[x][y] = squares.PLAYER2;
                }
                if(x == 1 && y % 2 == 0){
                    this.board[x][y] = squares.PLAYER2;
                }
                if(x == 3 && y % 2 == 0){
                    this.board[x][y] = squares.EMPTY;
                }
                if(x == 4 && y % 2 == 1){
                    this.board[x][y] = squares.EMPTY;
                }
                if((x == 5 || x == 7) && (y % 2 == 0)){
                    this.board[x][y] = squares.PLAYER1;
                }
                if(x == 6 && y % 2 == 1){
                    this.board[x][y] = squares.PLAYER1;
                }
            }
        }
    }

    public squares[][] getBoardPlayer1(){
        return this.board;
    }

    public squares[][] getBoardPlayer2(){
        int x2 = 0;
        int y2 = 0;
        squares[][] p2Board = new squares[8][8];
        for(int x1 = 0; x1 < 8; x1++){
            for(int y1 = 0; y1 < 8; y1++){
                x2 = 7 - x1;
                y2 = 7 - y1;
                p2Board[x2][y2] = this.board[x1][y1];
            }
        }
        return p2Board;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public squares getValueOnBoard(int x, int y){
        return this.board[x][y];
    }

    public void move(int x1, int y1, int x2, int y2){
        if(this.board[x1][y1] == squares.PLAYER1){
            if(this.board[x2][y2] == squares.EMPTY){
                this.board[x1][y1] = squares.EMPTY;
                this.board[x2][y2] = squares.PLAYER1;
            }
        }
        if(this.board[x1][y1] == squares.PLAYER2){
            if(this.board[x2][y2] == squares.EMPTY){
                this.board[x1][y1] = squares.EMPTY;
                this.board[x2][y2] = squares.PLAYER2;
            }
        }
    }
}
