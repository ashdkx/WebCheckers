package com.webcheckers.appl;

import com.webcheckers.model.*;

import java.util.*;

/**
 * @author Nicholas Curl
 */
public class GameBoard implements Iterable<Row> {

    private GameView game;
    private Player playerTurn;
    private boolean singleMove = false;
    private Piece activePiece = null;
    private Position activePieceStart;
    private Stack<Position> activePieceEnds = new Stack<>();
    private int activePieceMoves = 0;
    private ArrayList<int[]> pieceRemove = new ArrayList<>();
    private Map<int[], List<int[]>> requiredMovePieces = new HashMap<>();
    private int redPlayerTotalPieces = 12;
    private int whitePlayerTotalPieces = 12;

    public enum color{RED, WHITE}

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

    private List<Row> getRedPlayerBoard(){
        return game.getRedPlayerBoard();
    }

    private List<Row> getWhitePlayerBoard(){
        return game.getWhitePlayerBoard();
    }

    public boolean isRedPlayer(Player player){
        return player.equals(getRedPlayer());
    }

    public void setPlayerTurn(Player playerTurn) {
        this.playerTurn = playerTurn;
    }

    public boolean isMyTurn(Player player){
        return player.equals(playerTurn);
    }

    public Player getPlayerTurn(){
        return playerTurn;
    }

    public List<Row> getPlayerBoard(Player player){
        if(this.isRedPlayer(player)){
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

    public void setPieceKing(Player player, List<Row> playerBoard, int row, int col){
        switch (getPlayerColor(player)){
            case RED:
                setPiece(playerBoard,row,col,Piece.redKing);
                break;
            case WHITE:
                setPiece(playerBoard,row,col,Piece.whiteKing);
        }
    }

    public void setSingleMove(boolean singleMove){
        this.singleMove = singleMove;
    }

    public boolean isSingleMove() {
        return singleMove;
    }

    public boolean isNotPlayerColor(Piece piece, Player player){
        boolean valid = false;
        if(piece != null) {
            switch (piece.getColor()) {
                case RED:
                    if (!isRedPlayer(player)) { // if player's color equals white and the piece's color is red return true
                        valid = true;
                    }
                    break;
                case WHITE:
                    if (isRedPlayer(player)) { // if player's color equal red and the piece's color is white return true
                        valid = true;
                    }
                    break;
            }
        }
        return valid;
    }

    /**
     * Gets the players color
     * @param player player to check
     * @return color based on if the player is red or not
     */
    public color getPlayerColor(Player player){
        if(isRedPlayer(player)){ //if the player is the red player return the enum RED
            return color.RED;
        }
        else { //else return the enum WHITE
            return color.WHITE;
        }
    }


    /**
     * Gets the total number of red pieces
     * @return total red pieces
     */
    public int getRedPlayerTotalPieces() {
        return redPlayerTotalPieces;
    }


    /**
     *
     * @param player: add the piece according to the players color
     */
    public void addPlayerTotalPieces(Player player){
        if(isRedPlayer(player)){ // checks to see if the player is the red player
            redPlayerTotalPieces++; //adds one piece
        }
        else {
            whitePlayerTotalPieces++;
        }
    }

    /**
     *
     * @param player get the opponent based on the player
     * @param amount amount to remove
     */
    public void removeOpponentTotalPieces(Player player, int amount){
        if(!isRedPlayer(player)){ // checks to see if the player is not the red player, meaning player2 is red
            redPlayerTotalPieces-=amount; //remove total pieces by amount
        }
        else {
            whitePlayerTotalPieces-=amount;
        }
    }

    /**
     * Gets the total white pieces
     * @return total white pieces
     */
    public int getWhitePlayerTotalPieces() {
        return whitePlayerTotalPieces;
    }


    @Override
    public Iterator<Row> iterator(){
        return game.getBoard().iterator();
    }
}
