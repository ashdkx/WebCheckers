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
    private Stack<Position> activePieceEnds = new Stack<>();
    private int activePieceMoves = 0;
    private ArrayList<int[]> pieceRemove = new ArrayList<>();
    private Map<int[], List<int[]>> requiredMovePieces = new HashMap<>();
    private List<int[]> jumpPositions = new ArrayList<>();

    public enum color{
        RED,
        WHITE
    }

    public GameBoard(Player redPlayer, Player whitePlayer){
        this.game = new GameView(redPlayer,whitePlayer);
    }

    public GameView getGame() {
        return game;
    }

    public Player getRedPlayer(){
        return game.getRedPlayer();
    }

    public Player getWhitePlayer(){
        return game.getWhitePlayer();
    }

    public List<Row> getRedPlayerBoard(){
        return game.getRedPlayerBoard();
    }

    public List<Row> getWhitePlayerBoard(){
        return game.getWhitePlayerBoard();
    }

    public List<Row> getPlayerBoard(Player player){
        if(player.isRedPlayer()){
            return this.getRedPlayerBoard();
        }
        else{
            return this.getWhitePlayerBoard();
        }
    }

    public void isWhitePlayerBoard(boolean whiteBoard){
        game.isWhitePlayerBoard(whiteBoard);
    }

    public void updateRedPlayer(){
        game.updateRedPlayer();
    }

    public void updateWhitePlayer(){
        game.updateWhitePlayer();
    }

    public List<Row> getBoard(){
        return game.getBoard();
    }

    public boolean isValidSpace(List<Row> board, int row, int col){
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

    public Piece getActivePiece() {
        return activePiece;
    }

    public void setActivePieceStart(Position activePieceStart) {
        this.activePieceStart = activePieceStart;
    }

    public Position getActivePieceStart(){
        return this.activePieceStart;
    }

    public void addActivePieceEnd(Position activePieceEnd) {
        this.activePieceEnds.push(activePieceEnd);
    }

    public Position getActivePieceEnd(){
        return this.activePieceEnds.pop();
    }

    public void clearActivePieceEnd(){
        this.activePieceEnds.clear();
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

    public List<int[]> getJumpPositions() {
        return jumpPositions;
    }

    public void addJumpPosition(int[] jump){
        jumpPositions.add(jump);
    }

    public void clearJumpPositions(){
        jumpPositions.clear();
    }

    @Override
    public Iterator<Row> iterator(){
        return game.getBoard().iterator();
    }
}
