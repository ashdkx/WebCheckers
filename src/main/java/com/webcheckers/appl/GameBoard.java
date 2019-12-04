package com.webcheckers.appl;

import com.webcheckers.model.*;

import java.util.*;

/**
 * The representation of and controller of the Game Board
 *
 * @author Nicholas Curl
 */
public class GameBoard implements Iterable<Row> {

    /**
     * The game view associated to the GameBoard
     */
    private GameView game;

    /**
     * The player whose turn it is
     */
    private Player playerTurn;

    /**
     * Is the move a single diagonal
     */
    private boolean singleMove = false;

    /**
     * The active piece being moved
     */
    private Piece activePiece = null;

    /**
     * The starting position of the active piece
     */
    private Position activePieceStart;

    /**
     * The stack of end positions of the active piece
     */
    private Stack<Position> activePieceEnds = new Stack<>();

    /**
     * The total moves for the active piece
     */
    private int activePieceMoves = 0;

    /**
     * The pieces to be removed
     */
    private ArrayList<int[]> pieceRemove = new ArrayList<>();

    /**
     * the count of the moves made for spectator mode
     */
    private int turnCount = 0;

    /**
     * The pieces that are required to move
     */
    private Map<int[], List<int[]>> requiredMovePieces = new HashMap<>();

    /**
     * The total amount of red pieces
     */
    private int redPlayerTotalPieces = 12;

    /**
     * The total amount of white pieces
     */
    private int whitePlayerTotalPieces = 12;

    /**
     * The positions of pieces that can be jumped
     */
    private List<int[]> jumpPositions = new ArrayList<>();

    /**
     * Is the game over
     */
    private boolean gameOver = false;

    /**
     * The message associated to game over
     */
    private String gameOverMessage = "";

    /**
     * Has a player resigned
     */
    private boolean resigned = false;

    /**
     * A list to store the moves made
     */
    private ArrayList<MoveSave> moves = new ArrayList<>();

    /**
     * Is the active piece supposed to be king
     */
    private boolean activePieceCrown = false;

    /**
     * The colors of the players
     */
    public enum color {RED, WHITE}

    /**
     * Constructor for creating a new GameBoard to play on
     *
     * @param redPlayer   The player to be the redPlayer
     * @param whitePlayer The player to be the whitePlayer
     */
    public GameBoard(Player redPlayer, Player whitePlayer) {
        this.game = new GameView(redPlayer, whitePlayer);
    }

    /**
     * Gets the game view
     *
     * @return The game view
     */
    public GameView getGame() {
        return game;
    }

    /**
     * Gets the red player
     *
     * @return The red player
     */
    public Player getRedPlayer() {
        return game.getRedPlayer();
    }

    /**
     * Gets the white player
     *
     * @return The white player
     */
    public Player getWhitePlayer() {
        return game.getWhitePlayer();
    }

    /**
     * Get the red player's board
     *
     * @return The red player's board
     */
    private List<Row> getRedPlayerBoard() {
        return game.getRedPlayerBoard();
    }

    /**
     * Gets the white player's board
     *
     * @return The white player's board
     */
    private List<Row> getWhitePlayerBoard() {
        return game.getWhitePlayerBoard();
    }

    /**
     * Checks to see if the player is the red player
     *
     * @param player The player to check
     * @return True if the player is the red player, false otherwise
     */
    public boolean isRedPlayer(Player player) {
        return player.equals(getRedPlayer());
    }

    /**
     * Sets the player whose turn it is
     *
     * @param playerTurn The player whose turn it is
     */
    public void setPlayerTurn(Player playerTurn) {
        this.playerTurn = playerTurn;
    }

    /**
     * Checks to see if it is currently the player's turn
     *
     * @param player The player to check
     * @return True if it is the player's turn, false otherwise
     */
    public boolean isMyTurn(Player player) {
        return player.equals(playerTurn);
    }

    /**
     * Gets which player has the active turn
     *
     * @return The player whose turn it is
     */
    public Player getPlayerTurn() {
        return playerTurn;
    }

    /**
     * Gets the player board based on the player's color
     *
     * @param player The player to check
     * @return The player board based on the player's color
     */
    public List<Row> getPlayerBoard(Player player) {
        if (this.isRedPlayer(player)) {
            return this.getRedPlayerBoard();
        } else {
            return this.getWhitePlayerBoard();
        }
    }

    /**
     * Toggles which board to display
     *
     * @param whiteBoard Is the white board to be displayed
     */
    public void isWhitePlayerBoard(boolean whiteBoard) {
        game.isWhitePlayerBoard(whiteBoard);
    }

    /**
     * Update the red player's board
     */
    public void updateRedPlayer() {
        game.updateRedPlayer();
    }

    /**
     * Updates the white player's board
     */
    public void updateWhitePlayer() {
        game.updateWhitePlayer();
    }

    /**
     * Gets the board
     *
     * @return The board
     */
    public List<Row> getBoard() {
        return game.getBoard();
    }

    /**
     * Checks to see if the space is valid
     *
     * @param board The board to base the coordinates on
     * @param row   The row to check
     * @param col   The column to check
     * @return True if the space is valid false otherwise
     */
    public boolean isValidSpace(List<Row> board, int row, int col) {
        if (row > 7 || row < 0 || col > 7 || col < 0) { //checks to see if space is outside the board
            return false;
        } else {
            return board.get(row).getSpace(col).isValid();
        }
    }

    /**
     * Gets the piece at the specified location based on the current player's view
     *
     * @param board The board to base the coordinates on
     * @param row   The row to get the piece
     * @param col   The column to get the piece
     * @return The piece at the row and column
     */
    public Piece getPiece(List<Row> board, int row, int col) {
        if (row > 7 || row < 0 || col > 7 || col < 0) { //checks to see if the piece is outside the board
            return null;
        } else {
            return board.get(row).getSpace(col).getPiece();
        }
    }

    /**
     * Sets the piece at specific row and column based on the current player's view
     *
     * @param board The board to base the coordinates on
     * @param row   The row number for the piece to be set
     * @param col   The column number for the piece to be set
     * @param piece The piece to be set
     */
    public void setPiece(List<Row> board, int row, int col, Piece piece) {
        if (!(row > 7 || row < 0 || col > 7 || col < 0)) { //checks to see if the piece is outside the board
            board.get(row).getSpace(col).setPiece(piece);
        }
    }

    /**
     * Sets the active piece
     *
     * @param piece The piece to be active
     */
    public void setActivePiece(Piece piece) {
        this.activePiece = piece;
    }

    /**
     * Gets the active piece
     *
     * @return The active piece
     */
    public Piece getActivePiece() {
        return activePiece;
    }

    /**
     * Sets the start of the active piece
     *
     * @param activePieceStart Position of the active piece start
     */
    public void setActivePieceStart(Position activePieceStart) {
        this.activePieceStart = activePieceStart;
    }

    /**
     * Gets the starting position of the active piece
     *
     * @return The starting position of the active piece
     */
    public Position getActivePieceStart() {
        return this.activePieceStart;
    }

    /**
     * Adds an end position to the active piece
     *
     * @param activePieceEnd The position of the new end
     */
    public void addActivePieceEnd(Position activePieceEnd) {
        this.activePieceEnds.push(activePieceEnd);
    }

    /**
     * Gets the active piece end from the top of the stack
     *
     * @return The active piece end position from the top of the stack
     */
    public Position getActivePieceEnd() {
        return this.activePieceEnds.pop();
    }

    /**
     * Clears end positions of the active piece
     */
    public void clearActivePieceEnd() {
        this.activePieceEnds.clear();
    }

    /**
     * Gets the total number of active piece moves
     *
     * @return The total active piece moves
     */
    public int getActivePieceMoves() {
        return activePieceMoves;
    }

    /**
     * Sets the number of moves the active piece has
     *
     * @param activePieceMoves The amount of moves
     */
    public void setActivePieceMoves(int activePieceMoves) {
        this.activePieceMoves = activePieceMoves;
    }

    /**
     * Increments the total active piece moves
     */
    public void incrementActivePieceMoves() {
        this.activePieceMoves++;
    }

    /**
     * Decrements the total active piece moves
     */
    public void decrementActivePieceMoves() {
        this.activePieceMoves--;
    }

    /**
     * Add piece to piece remove
     *
     * @param position Position of the piece to remove
     */
    public void addPieceRemove(int[] position) {
        pieceRemove.add(position);
    }

    /**
     * Remove piece from piece remove
     *
     * @return The removed piece
     */
    public int[] removePieceRemove() {
        return pieceRemove.remove(pieceRemove.size() - 1);
    }

    /**
     * Gets the position of the piece to remove
     *
     * @return The position of the piece to remove
     */
    public ArrayList<int[]> getPieceRemove() {
        return pieceRemove;
    }

    /**
     * Gets the map of all the required move pieces
     *
     * @return Map of the required move pieces
     */
    public Map<int[], List<int[]>> getRequiredMovePieces() {
        return requiredMovePieces;
    }

    /**
     * Checks to see if the piece at certain position is one of the required move pieces
     *
     * @param position Position of the piece
     * @return True if it is a required move piece, false otherwise
     */
    public boolean isRequiredMovePiece(int[] position) {
        boolean valid = false;
        for (int[] requiredPosition : requiredMovePieces.keySet()) {
            if (position[0] == requiredPosition[0] && position[1] == requiredPosition[1]) {
                valid = true;
                break;
            }
        }
        return valid;
    }

    /**
     * Adds a piece to the required moves including the possible jumps
     *
     * @param position The position of the piece to add to the required moves
     * @param jumps    The list of possible jumps for the piece
     */
    public void addRequiredMovePieces(int[] position, List<int[]> jumps) {
        requiredMovePieces.put(position, jumps);
    }

    /**
     * Clears the pieces that are required to move
     */
    public void clearRequiredMovePieces() {
        requiredMovePieces.clear();
    }

    /**
     * Get the list of required moves for the piece at a certain position
     *
     * @param position The array of the position of the piece
     * @return The list of the required move jumps
     */
    public List<int[]> getRequiredMoveJumps(int[] position) {
        List<int[]> jumpPositions = new ArrayList<>();
        for (int[] requiredPosition : requiredMovePieces.keySet()) {
            if (position[0] == requiredPosition[0] && position[1] == requiredPosition[1]) {
                jumpPositions = requiredMovePieces.get(requiredPosition);
            }
        }
        return jumpPositions;
    }

    /**
     * Sets the piece relative to the player to be king
     *
     * @param player      The player
     * @param playerBoard The board associated to the player
     * @param row         Row number of the piece to crown
     * @param col         Column number of the piece to crown
     */
    public void setPieceKing(Player player, List<Row> playerBoard, int row, int col) {
        switch (getPlayerColor(player)) {
            case RED:
                setPiece(playerBoard, row, col, Piece.redKing);
                break;
            case WHITE:
                setPiece(playerBoard, row, col, Piece.whiteKing);
        }
    }

    /**
     * Sets the move to be a single diagonal move
     *
     * @param singleMove True if the move is a single one false otherwise
     */
    public void setSingleMove(boolean singleMove) {
        this.singleMove = singleMove;
    }

    /**
     * Is the move a single move?
     *
     * @return Single move
     */
    public boolean isSingleMove() {
        return singleMove;
    }

    /**
     * Checks to see if the piece does not match the player's color
     *
     * @param piece  The piece to check
     * @param player The player to compare to
     * @return True if the piece is not the same as the player's color false otherwise
     */
    public boolean isNotPlayerColor(Piece piece, Player player) {
        boolean valid = false;
        if (piece != null) {
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
     *
     * @param player Player to check
     * @return Color based on if the player is red or not
     */
    public color getPlayerColor(Player player) {
        if (isRedPlayer(player)) { //if the player is the red player return the enum RED
            return color.RED;
        } else { //else return the enum WHITE
            return color.WHITE;
        }
    }

    /**
     * Gets the total red pieces
     *
     * @return Total red pieces
     */
    public int getRedPlayerTotalPieces() {
        return redPlayerTotalPieces;
    }


    /**
     * Adds a piece to the current player's color
     *
     * @param player Add the piece according to the players color
     */
    public void addPlayerTotalPieces(Player player) {
        if (isRedPlayer(player)) { // checks to see if the player is the red player
            redPlayerTotalPieces++; //adds one piece
        } else {
            whitePlayerTotalPieces++;
        }
    }

    /**
     * Removes the amount of pieces from the player's opponent's total pieces
     *
     * @param player Get the opponent based on the player
     * @param amount Amount to remove
     */
    public void removeOpponentTotalPieces(Player player, int amount) {
        if (!isRedPlayer(player)) { // checks to see if the player is not the red player, meaning player2 is red
            redPlayerTotalPieces -= amount; //remove total pieces by amount
        } else {
            whitePlayerTotalPieces -= amount;
        }
    }

    /**
     * Gets the total white Pieces
     *
     * @return Total white pieces
     */
    public int getWhitePlayerTotalPieces() {
        return whitePlayerTotalPieces;
    }

    /**
     * Get the list of positions of pieces to jump
     *
     * @return List of positions of pieces to jump
     */
    public List<int[]> getJumpPositions() {
        return jumpPositions;
    }

    /**
     * Checks to see if a piece is able to jump
     *
     * @param player      The current player
     * @param playerBoard The board that is associated to the current player
     * @param row         Row number of the piece
     * @param col         Column number of the piece
     * @param piece       The piece
     * @return True if able to jump false otherwise
     *
     * <pre>
     * Structure:
     *             201
     *             0P0
     *             403
     *
     *             0 = invalid space
     *             1 = validPos1
     *             2 = validPos2
     *             3 = validPos3
     *             4 = validPos4
     *             P = piece being checked
     * </pre>
     */
    public boolean canJump(Player player, List<Row> playerBoard, int row, int col, Piece piece) {
        boolean validPos1 = false;
        boolean validPos2 = false;
        boolean validPos3 = false;
        boolean validPos4 = false;

        if (getPiece(playerBoard, row - 1, col + 1) != null) {
            Piece pieceJump = getPiece(playerBoard, row - 1, col + 1); //get piece at validPos1
            boolean isCorrectColor = isNotPlayerColor(pieceJump, player); //check if piece at validPos1 is the opponent color
            boolean isValidSpace = isValidSpace(playerBoard, row - 2, col + 2); //Check to see if open space to jump to
            validPos1 = isValidSpace && isCorrectColor && !hasJumpedPiece(row -1, col+1);
            if (validPos1) {
                jumpPositions.add(new int[]{row - 1, col + 1}); //if it is valid add it to possible jumps
            }
        }
        if (getPiece(playerBoard, row - 1, col - 1) != null) {
            Piece pieceJump = getPiece(playerBoard, row - 1, col - 1); //get piece at validPos2
            boolean isCorrectColor = isNotPlayerColor(pieceJump, player); //check if piece at validPos2 is the opponent color
            boolean isValidSpace = isValidSpace(playerBoard, row - 2, col - 2); //Check to see if open space to jump to
            validPos2 = isCorrectColor && isValidSpace && !hasJumpedPiece(row -1, col-1);

            if (validPos2) {
                jumpPositions.add(new int[]{row - 1, col - 1}); //if it is valid add it to possible jumps
            }

        }

        if (piece.getType() == Piece.type.KING || (activePieceCrown&& (row==activePieceStart.getRow()||row==activePieceEnds.peek().getRow())&& (col==activePieceStart.getCell()||col==activePieceEnds.peek().getCell())) ){ // check to see if the piece being checked is a King type
            if (getPiece(playerBoard, row + 1, col + 1) != null) {
                Piece pieceJump = getPiece(playerBoard, row + 1, col + 1); //get piece at validPos3
                boolean isCorrectColor = isNotPlayerColor(pieceJump, player); //check if piece at validPos3 is the opponent color
                boolean isValidSpace = isValidSpace(playerBoard, row + 2, col + 2); //Check to see if open space to jump to
                validPos3 = isValidSpace && isCorrectColor && !hasJumpedPiece(row +1, col+1);
                if (validPos3) {
                    jumpPositions.add(new int[]{row + 1, col + 1}); //if it is valid add it to possible jumps
                }
            }
            if (getPiece(playerBoard, row + 1, col - 1) != null) {
                Piece pieceJump = getPiece(playerBoard, row + 1, col - 1); //get piece at validPos4
                boolean isCorrectColor = isNotPlayerColor(pieceJump, player); //check if piece at validPos4 is the opponent color
                boolean isValidSpace = isValidSpace(playerBoard, row + 2, col - 2); //Check to see if open space to jump to
                validPos4 = isValidSpace && isCorrectColor && !hasJumpedPiece(row +1, col-1);
                if (validPos4) {
                    jumpPositions.add(new int[]{row + 1, col - 1}); //if it is valid add it to possible jumps
                }
            }
        }
        return validPos1 || validPos2 || validPos3 || validPos4;
    }

    /**
     * Checks to see if a piece can move diagonally once
     *
     * @param playerBoard Board used for reference
     * @param row         Row number of the piece
     * @param col         Column number of the piece
     * @param piece       The piece
     * @return True if can move diagonally once false otherwise
     *
     * <pre>
     * Structure:
     *
     *             201
     *             0P0
     *             403
     *
     *             0 = invalid space
     *             1 = validPos1
     *             2 = validPos2
     *             3 = validPos3
     *             4 = validPos4
     *             P = piece being checked
     * </pre>
     */
    private boolean canMove(List<Row> playerBoard, int row, int col, Piece piece) {
        boolean validPos1 = false;
        boolean validPos2 = false;
        boolean validPos3 = false;
        boolean validPos4 = false;

        if (isValidSpace(playerBoard, row - 1, col + 1)) { // check to see if validPos1 is a valid space
            validPos1 = true;
        }
        if (isValidSpace(playerBoard, row - 1, col - 1)) { // check to see if validPos2 is a valid space
            validPos2 = true;
        }

        if (piece.getType() == Piece.type.KING || (activePieceCrown&& (row==activePieceStart.getRow()||row==activePieceEnds.peek().getRow())&& (col==activePieceStart.getCell()||col==activePieceEnds.peek().getCell()))) { // check to see if the piece being checked is a King type
            if (isValidSpace(playerBoard, row + 1, col + 1)) { // check to see if validPos3 is a valid space
                validPos3 = true;
            }
            if (isValidSpace(playerBoard, row + 1, col - 1)) { // check to see if validPos4 is a valid space
                validPos4 = true;
            }
        }
        return validPos1 || validPos2 || validPos3 || validPos4;
    }

    /**
     * Checks to see if there are any remaining moves for the player
     *
     * @param player The player to check
     * @return True if there are moves still available false otherwise
     */
    private boolean hasMove(Player player) {
        List<Row> playerBoard = getPlayerBoard(player);
        for (int i = 7; i > -1; i--) { //go through the entire board
            for (int j = 0; j < 8; j++) {
                Piece piece = getPiece(playerBoard, i, j);
                if (isNotPlayerColor(piece, player) || piece == null) { //if the piece is not the players color or there is no piece continue on
                    continue;
                }
                if (canMove(playerBoard, i, j, piece) || canJump(player, playerBoard, i, j, piece)) { //checks to see if all the current player's pieces have any moves
                    return true;
                }
            }
        }

        return false;

    }

    /**
     * Checks if the game should be over
     *
     * @return True if it should false otherwise
     */
    public boolean checkGameOver() {
        if (resigned) { // checks to see if a player has resigned
            gameOver = true;
        } else if (redPlayerTotalPieces <= 0) { // check to see if there are no remaining red pieces
            gameOver = true;
            gameOverMessage = getWhitePlayer().getName() + " has captured all pieces.";
        } else if (whitePlayerTotalPieces <= 0) { //checks to see if there are no remaining white pieces
            gameOver = true;
            gameOverMessage = getRedPlayer().getName() + " has captured all pieces.";
        } else if (!hasMove(getRedPlayer())) { // checks if there any available moves for the red player
            gameOver = true;
            String message = getRedPlayer().getName() + " does not have any remaining moves.";
            if (redPlayerTotalPieces > whitePlayerTotalPieces) { // checks if there are more red pieces than white
                gameOverMessage = message + " " + getRedPlayer().getName() + " has more pieces remaining.";
            } else if (redPlayerTotalPieces == whitePlayerTotalPieces) { // checks to see if both total red and white pieces are the same
                gameOverMessage = message + " It's a tie.";
            } else { // there are more white pieces than red
                gameOverMessage = message + " " + getWhitePlayer().getName() + " has more pieces remaining.";
            }
        } else if (!hasMove(getWhitePlayer())) { // checks if there any available moves for the white player
            gameOver = true;
            String message = getWhitePlayer().getName() + " does not have any remaining moves";
            if (whitePlayerTotalPieces > redPlayerTotalPieces) { // checks if there are more white pieces than red
                gameOverMessage = message + " " + getWhitePlayer().getName() + " has more pieces remaining.";
            } else if (redPlayerTotalPieces == whitePlayerTotalPieces) { // checks to see if both total red and white pieces are the same
                gameOverMessage = message + " It's a tie.";
            } else { // there are more red pieces than white
                gameOverMessage = message + " " + getRedPlayer().getName() + " has more pieces remaining.";
            }
        } else { // game is not over
            gameOver = false;
            gameOverMessage = "";
        }
        return gameOver;
    }

    /**
     * Is the game over
     *
     * @return Game over
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Resign the game
     *
     * @param player The player that is resigning
     */
    public void resign(Player player) {
        gameOverMessage = player.getName() + " has resigned.";
        resigned = true; // sets the game to be resigned
        moves.get(moves.size() - 1).setGameOverMessage(gameOverMessage); // set the new gameover message to the latest move
        player.setPlaying(false);
    }

    /**
     * Checks to see if the a player has resigned
     *
     * @return Resigned
     */
    public boolean hasResigned() {
        return resigned;
    }

    /**
     * Gets the game over message
     *
     * @return The game over message
     */
    public String getGameOverMessage() {
        return gameOverMessage;
    }

    /**
     * Creates a new saved move and adds it to the moves list
     */
    public void addMove() {
        MoveSave move = new MoveSave(getRedPlayerBoard(), getPlayerColor(playerTurn), gameOverMessage);
        turnCount++;
        moves.add(move);
    }

    /**
     * Get the list of moves
     *
     * @return The list of moves
     */
    public ArrayList<MoveSave> getMoves() {
        return moves;
    }


    /**
     * Checks to see if the active piece should be crowned and sets it to true if it is
     */
    public void checkActivePieceCrown() {
        for (Position ends : activePieceEnds) {
            if (ends.getRow() == 0&&activePiece.getType()== Piece.type.SINGLE) {
                activePieceCrown = true;
                break;
            }
            else {
                activePieceCrown = false;
            }
        }
    }

    public void setActivePieceCrown(boolean crown) {
        this.activePieceCrown = crown;
    }

    public boolean isActivePieceCrown() {
        return activePieceCrown;
    }

    public Stack<Position> getActivePieceEnds() {
        return activePieceEnds;
    }

    private boolean hasJumpedPiece(int row, int column){
        boolean jumpedPiece = false;
        for (int[] positions:pieceRemove){
            if(row == positions[0] && column == positions[1]){
                jumpedPiece = true;
                break;
            }
        }
        return jumpedPiece;
    }

    /**
     * Iterates the board for displaying
     *
     * @return The iterator of the board
     */
    @Override
    public Iterator<Row> iterator() {
        return game.getBoard().iterator();
    }

    /**
     * returns turn count for spectator mode
     *
     * @return turn count
     */
    public int getTurnCount() {
        return turnCount;
    }
}
