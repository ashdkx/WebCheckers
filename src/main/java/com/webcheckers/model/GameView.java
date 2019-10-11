package com.webcheckers.model;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class GameView {
    public enum squares{
        EMPTY,
        INVALID,
        PLAYER1,
        PLAYER2,
    }

    //private squares[][] board;
    private Player player1;
    private Player player2;
    private List<Row> board;


    public GameView(Player player1, Player player2) {
        //this.board = new squares[8][8];
        this.player1 = player1;
        this.player2 = player2;
        this.board = new LinkedList<>();
        initBoard();
    }


    public List<Row> getBoard() {
        return board;
    }

    public void initBoard(){
        boolean valid = true;

        for(int i =0; i<8;i++){
            board.add( new Row(i, valid));
            valid = !valid;
        }
    }

  /*  public squares[][] getBoardPlayer1(){
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
    }*/
}