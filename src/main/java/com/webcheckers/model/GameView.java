package com.webcheckers.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Stack;


/**
 * @author Nicholas Curl
 */
public class GameView {


    private Player player1;
    private Player player2;
    private List<Row> board;
    private List<Row> player1Board = new ArrayList<>();
    private List<Row> player2Board = new ArrayList<>();




    public GameView(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = new ArrayList<>();
        initBoard();
    }


    public List<Row> getBoard() {
        return board;
    }

    public void setBoard(List<Row> board){
        this.board = board;
    }

    private void initBoard(){
        initPlayer1();
        initPlayer2();
    }

    private void initPlayer1(){
        boolean valid1 = false;

        for(int i = 0; i < 8; i++){
            if (i<=2){
                player1Board.add(i,new Row(i,Piece.whiteSingle,valid1));
            }
            else if (i>=5){
                player1Board.add(i, new Row(i,Piece.redSingle,valid1));
            }
            else{
                player1Board.add(i, new Row(i,null,valid1));
            }
            valid1 = !valid1;
        }
    }

    private void initPlayer2(){
        boolean valid2 = false;

        for(int i = 0; i < 8; i++){
            if (i<=2){
                player2Board.add(i,new Row(i,Piece.redSingle,valid2));
            }
            else if (i>=5){
                player2Board.add(i, new Row(i,Piece.whiteSingle,valid2));
            }
            else{
                player2Board.add(i, new Row(i,null,valid2));
            }
            valid2 = !valid2;
        }
    }
    public void isPlayer2Board(boolean board2){
        if(board2){

            this.board = player2Board;
        }
        else {
            this.board = player1Board;
        }
    }

    public void updatePlayer2(){
        for(int i = 0; i<8;i++){
            for (int j = 0; j<8;j++){
                player2Board.get(i).getSpace(7-j).setPiece(player1Board.get(7-i).getSpace(j).getPiece());
            }
        }
    }
    public void updatePlayer1(){
        for(int i = 0; i<8;i++){
            for (int j = 0; j<8;j++){
                player1Board.get(i).getSpace(7-j).setPiece(player2Board.get(7-i).getSpace(j).getPiece());
            }
        }
    }

    public List<Row> getPlayer1Board(){
        return player1Board;
    }

    public List<Row> getPlayer2Board() {
        return player2Board;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }



}