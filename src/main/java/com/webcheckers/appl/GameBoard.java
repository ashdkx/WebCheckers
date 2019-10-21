package com.webcheckers.appl;

import com.webcheckers.model.*;

import java.util.Iterator;
import java.util.List;

/**
 * @author Nicholas Curl
 */
public class GameBoard implements Iterable<Row> {

    private GameView game;

    public enum color{
        RED,
        WHITE
    }

    public GameBoard(Player player1, Player player2){
        this.game = new GameView(player1,player2);
    }


    public GameView getGame() {
        return game;
    }

    public Player getPlayer1(){
        return game.getPlayer1();
    }

    public Player getPlayer2(){
        return game.getPlayer2();
    }

    public List<Row> getPlayer1Board(){
        return game.getPlayer1Board();
    }

    public List<Row> getPlayer2Board(){
        return game.getPlayer2Board();
    }

    public void isPlayer2Board(boolean board2){
        game.isPlayer2Board(board2);
    }

    public boolean isValid(List<Row> board, int row, int col){
        return game.isValid(board, row,col);
    }

    public Piece getPiece(int row, int col){
        return game.getPiece(row, col);
    }

    public void setActivePiece(Piece piece){
        game.setActivePiece(piece);
    }

    public Piece getActivePiece(){
        return game.getActivePiece();
    }

    public List<Row> getBoard(){
        return game.getBoard();
    }


    public Position getActiveStart(){
        return game.getActiveStart();
    }

    public void setActivePieceEnd(Position end){
        game.setActivePieceEnd(end);
    }

    public void setActivePieceStart(Position start){
        game.setActivePieceStart(start);
    }

    public Position getActiveEnd(){
        return game.getActiveEnd();
    }

    public void updatePlayer1(){
        game.updatePlayer1();
    }

    public void updatePlayer2(){
        game.updatePlayer2();
    }


    @Override
    public Iterator<Row> iterator(){
        return game.getBoard().iterator();
    }
}
