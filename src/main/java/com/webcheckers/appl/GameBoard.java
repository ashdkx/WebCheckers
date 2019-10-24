package com.webcheckers.appl;

import com.webcheckers.model.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * @author Nicholas Curl
 */
public class GameBoard implements Iterable<Row> {

    private GameView game;
    private Piece activePiece = null;
    private Position activePieceStart;
    private Position activePieceEnd;
    private int activePieceMoves = 0;
    private Stack<Integer[]> pieceRemove = new Stack<>();

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

    public void updatePlayer1(){
        game.updatePlayer1();
    }

    public void updatePlayer2(){
        game.updatePlayer2();
    }

    public List<Row> getBoard(){
        return game.getBoard();
    }

    public boolean isValid(List<Row> board, int row, int col){
        if(row>7||row<0||col>7||col<0){
            return false;
        }
        else {
            return board.get(row).getSpace(col).isValid();
        }
    }

    public Piece getPiece(List<Row> board, int row, int col){
        if(row>7||row<0||col>7||col<0){
            return null;
        }
        else {
            return board.get(row).getSpace(col).getPiece();
        }
    }

    public void setPiece(List<Row> board, int row, int col, Piece piece){
        if(!(row>7||row<0||col>7||col<0)) {
            board.get(row).getSpace(col).setPiece(piece);
        }
    }

    public void setActivePiece(Piece piece) {
        this.activePiece = piece;

    }

    public void setActivePieceStart(Position activePieceStart) {
        this.activePieceStart = activePieceStart;
    }

    public Piece getActivePiece() {
        return activePiece;
    }

    public Position getActiveStart(){
        return this.activePieceStart;
    }

    public void setActivePieceEnd(Position activePieceEnd) {
        this.activePieceEnd = activePieceEnd;
    }

    public Position getActiveEnd(){
        return this.activePieceEnd;
    }


    public int getActivePieceMoves() {
        return activePieceMoves;
    }

    public void setActivePieceMoves(int activePieceMoves) {
        this.activePieceMoves = activePieceMoves;
    }

    public void incrementActivePieceMoves(){
        this.activePieceMoves++;
    }

    public void decrementActivePieceMoves(){
        this.activePieceMoves--;
    }

    public void addPieceRemove(Integer[] position){
        pieceRemove.push(position);
    }

    public Integer[] removePieceRemove(){
        return pieceRemove.pop();
    }

    public Stack<Integer[]> getPieceRemove(){
        return pieceRemove;
    }

    @Override
    public Iterator<Row> iterator(){
        return game.getBoard().iterator();
    }
}
