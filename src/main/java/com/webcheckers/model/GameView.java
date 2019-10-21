package com.webcheckers.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Collections;


/**
 * @author Nicholas Curl
 */
public class GameView {


    private Player player1;
    private Player player2;
    private List<Row> board;
    private List<Row> player1Board =  new ArrayList<>();


    public GameView(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = new ArrayList<>();
        initBoard();
    }


    public List<Row> getBoard() {
        return board;
    }

    private void initBoard(){
        boolean valid = false;

        for(int i = 0; i < 8; i++){
            if (i<=2){
                board.add(new Row(i,Piece.whiteSingle,valid));
            }
            else if (i>=5){
                board.add(new Row(i,Piece.redSingle,valid));
            }
            else{
                board.add(new Row(i,null,valid));
            }
            valid = !valid;
        }
        board.get(4).getSpace(1).setPiece(Piece.whiteSingle);
    }

    public boolean isValid(int row, int col){

        return board.get(row).getSpace(col).isValid();

    }


    public Piece getPiece(int row, int col){
        return board.get(row).getSpace(col).getPiece();
    }


    public void setPlayer2Board(boolean playerBoard){
        if(playerBoard){
            reverseBoard();
        }
    }

    public void setBoard(List<Row> board){
        this.board = board;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

/*
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

   public void reverseBoard(){
       for(Row r : board){
           r.reverseRow();
       }

       Collections.reverse(board);
   }
}