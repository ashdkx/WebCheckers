package com.webcheckers.model;


import java.util.ArrayList;
import java.util.List;


/**
 * @author Nicholas Curl
 */
public class GameView {


    private Player redPlayer;
    private Player whitePlayer;
    private List<Row> board;
    private List<Row> redPlayerBoard = new ArrayList<>();
    private List<Row> whitePlayerBoard = new ArrayList<>();


    public GameView(Player player1, Player player2) {
        this.redPlayer = player1;
        this.whitePlayer = player2;
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
        initRedPlayer();
        initWhitePlayer();
    }

    private void initRedPlayer(){
        boolean valid1 = false;

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

    private void initWhitePlayer(){
        boolean valid2 = false;

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
    public void isWhitePlayerBoard(boolean whiteBoard){
        if(whiteBoard){

            this.board = whitePlayerBoard;
        }
        else {
            this.board = redPlayerBoard;
        }
    }

    public void updateWhitePlayer(){
        for(int i = 0; i<8;i++){
            for (int j = 0; j<8;j++){
                whitePlayerBoard.get(i).getSpace(7-j).setPiece(redPlayerBoard.get(7-i).getSpace(j).getPiece());
            }
        }
    }
    public void updateRedPlayer(){
        for(int i = 0; i<8;i++){
            for (int j = 0; j<8;j++){
                redPlayerBoard.get(i).getSpace(7-j).setPiece(whitePlayerBoard.get(7-i).getSpace(j).getPiece());
            }
        }
    }

    public List<Row> getRedPlayerBoard(){
        return redPlayerBoard;
    }

    public List<Row> getWhitePlayerBoard() {
        return whitePlayerBoard;
    }

    public Player getRedPlayer() {
        return redPlayer;
    }

    public Player getWhitePlayer() {
        return whitePlayer;
    }

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

}