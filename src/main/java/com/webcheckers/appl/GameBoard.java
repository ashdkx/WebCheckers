package com.webcheckers.appl;

import com.webcheckers.model.*;

import java.util.*;

/**
 * @author Nicholas Curl
 */
public class GameBoard implements Iterable<Row> {

    private GameView game;
    private Piece activePiece = null;
    private Position activePieceStart;
    private Position activePieceEnd;
    private int activePieceMoves = 0;
    private ArrayList<int[]> pieceRemove = new ArrayList<>();
    private Map<int[], List<int[]>> requiredMovePieces = new HashMap<>();
    private List<int[]> jumpPositions = new ArrayList<>();

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

    public void addPieceRemove(int[] position){
        pieceRemove.add(position);
    }

    public int[] removePieceRemove(){
        return pieceRemove.remove(pieceRemove.size()-1);
    }

    public ArrayList<int[]> getPieceRemove(){
        return pieceRemove;
    }


    public Map<int[], List<int[]>> getRequiredMovePieces() {
        return requiredMovePieces;
    }

    public boolean isRequiredMovePiece(int[] position){
        boolean valid = false;
        for(int[] requiredPosition:requiredMovePieces.keySet()){
            if(position[0]==requiredPosition[0]&&position[1]==requiredPosition[1]){
                valid = true;
                break;
            }
        }
        return valid;

    }

    public void addRequiredMovePieces(int[] position, List<int[]> jumps){
        requiredMovePieces.put(position, jumps);
    }

    public void clearRequiredMovePieces(){
        requiredMovePieces.clear();
    }

    public List<int[]> getRequiredMoveJumps(int[] position){
        List<int[]> jumpPositions = new ArrayList<>();
        for(int[] requiredPosition: requiredMovePieces.keySet()){
            if(position[0]==requiredPosition[0]&&position[1]==requiredPosition[1]){
                jumpPositions = requiredMovePieces.get(requiredPosition);
            }
        }
        return jumpPositions;
    }

    public List<Row> getPlayerBoard(Player player){
        if(player.isPlayer1()){
            return this.getPlayer1Board();
        }
        else{
            return this.getPlayer2Board();
        }
    }

    public List<int[]> getJumpPositions() {
        return jumpPositions;
    }

    public void addJumpPosition(int[] jump){
        jumpPositions.add(jump);
    }

    public void clearJumpPositions(){
        jumpPositions.clear();
    }


    public void setPieceKing(Player player, List<Row> playerBoard, int row, int col){
        switch (player.getColor()){
            case RED:
                setPiece(playerBoard,row,col,Piece.redKing);
                break;
            case WHITE:
                setPiece(playerBoard,row,col,Piece.whiteKing);
        }
    }

    @Override
    public Iterator<Row> iterator(){
        return game.getBoard().iterator();
    }
}
