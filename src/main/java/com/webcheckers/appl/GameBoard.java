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
    private List<int[]> jumpPositions = new ArrayList<>();
    private boolean gameOver = false;
    private String gameOverMessage;

    public enum color {RED, WHITE}

    public GameBoard(Player redPlayer, Player whitePlayer) {
        this.game = new GameView(redPlayer, whitePlayer);
    }

    public GameView getGame() {
        return game;
    }

    public Player getRedPlayer() {
        return game.getRedPlayer();
    }

    public Player getWhitePlayer() {
        return game.getWhitePlayer();
    }

    private List<Row> getRedPlayerBoard() {
        return game.getRedPlayerBoard();
    }

    private List<Row> getWhitePlayerBoard() {
        return game.getWhitePlayerBoard();
    }

    public boolean isRedPlayer(Player player) {
        return player.equals(getRedPlayer());
    }

    public void setPlayerTurn(Player playerTurn) {
        this.playerTurn = playerTurn;
    }

    public boolean isMyTurn(Player player) {
        return player.equals(playerTurn);
    }

    public Player getPlayerTurn() {
        return playerTurn;
    }

    public List<Row> getPlayerBoard(Player player) {
        if (this.isRedPlayer(player)) {
            return this.getRedPlayerBoard();
        } else {
            return this.getWhitePlayerBoard();
        }
    }

    public void isWhitePlayerBoard(boolean whiteBoard) {
        game.isWhitePlayerBoard(whiteBoard);
    }

    public void updateRedPlayer() {
        game.updateRedPlayer();
    }

    public void updateWhitePlayer() {
        game.updateWhitePlayer();
    }

    public List<Row> getBoard() {
        return game.getBoard();
    }

    public boolean isValidSpace(List<Row> board, int row, int col) {
        if (row > 7 || row < 0 || col > 7 || col < 0) {
            return false;
        } else {
            return board.get(row).getSpace(col).isValid();
        }
    }

    public Piece getPiece(List<Row> board, int row, int col) {
        if (row > 7 || row < 0 || col > 7 || col < 0) {
            return null;
        } else {
            return board.get(row).getSpace(col).getPiece();
        }
    }

    public void setPiece(List<Row> board, int row, int col, Piece piece) {
        if (!(row > 7 || row < 0 || col > 7 || col < 0)) {
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

    public Position getActivePieceStart() {
        return this.activePieceStart;
    }

    public void addActivePieceEnd(Position activePieceEnd) {
        this.activePieceEnds.push(activePieceEnd);
    }

    public Position getActivePieceEnd() {
        return this.activePieceEnds.pop();
    }

    public void clearActivePieceEnd() {
        this.activePieceEnds.clear();
    }

    public int getActivePieceMoves() {
        return activePieceMoves;
    }

    public void setActivePieceMoves(int activePieceMoves) {
        this.activePieceMoves = activePieceMoves;
    }

    public void incrementActivePieceMoves() {
        this.activePieceMoves++;
    }

    public void decrementActivePieceMoves() {
        this.activePieceMoves--;
    }

    public void addPieceRemove(int[] position) {
        pieceRemove.add(position);
    }

    public int[] removePieceRemove() {
        return pieceRemove.remove(pieceRemove.size() - 1);
    }

    public ArrayList<int[]> getPieceRemove() {
        return pieceRemove;
    }


    public Map<int[], List<int[]>> getRequiredMovePieces() {
        return requiredMovePieces;
    }

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
     * @param position the position of the piece to add to the required moves
     * @param jumps    the list of possible jumps for the piece
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
     * @param position the array of the position of the piece
     * @return the list of the required move jumps
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
     * @param player      the player
     * @param playerBoard the board associated to the player
     * @param row         row of the piece to crown
     * @param col         column of the piece to crown
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
     * Sets the move to be single
     *
     * @param singleMove true if the move is a single one false otherwise
     */
    public void setSingleMove(boolean singleMove) {
        this.singleMove = singleMove;
    }

    /**
     * @return is the move a single move
     */
    public boolean isSingleMove() {
        return singleMove;
    }


    /**
     * Checks to see if the piece does not match the player's color
     *
     * @param piece  the piece to check
     * @param player the player to compare to
     * @return true if the piece is not the same as the player's color false otherwise
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
     * @param player player to check
     * @return color based on if the player is red or not
     */
    public color getPlayerColor(Player player) {
        if (isRedPlayer(player)) { //if the player is the red player return the enum RED
            return color.RED;
        } else { //else return the enum WHITE
            return color.WHITE;
        }
    }


    /**
     * @return total red pieces
     */
    public int getRedPlayerTotalPieces() {
        return redPlayerTotalPieces;
    }


    /**
     * Adds a piece to the current player's color
     *
     * @param player: add the piece according to the players color
     */
    public void addPlayerTotalPieces(Player player) {
        if (isRedPlayer(player)) { // checks to see if the player is the red player
            redPlayerTotalPieces++; //adds one piece
        } else {
            whitePlayerTotalPieces++;
        }
    }

    /**
     * Removes the amount from the player's opponent
     *
     * @param player get the opponent based on the player
     * @param amount amount to remove
     */
    public void removeOpponentTotalPieces(Player player, int amount) {
        if (!isRedPlayer(player)) { // checks to see if the player is not the red player, meaning player2 is red
            redPlayerTotalPieces -= amount; //remove total pieces by amount
        } else {
            whitePlayerTotalPieces -= amount;
        }
    }

    /**
     * @return total white pieces
     */
    public int getWhitePlayerTotalPieces() {
        return whitePlayerTotalPieces;
    }

    public List<int[]> getJumpPositions() {
        return jumpPositions;
    }

    /**
     * Checks to see if a piece is able to jump
     *
     * @param player      the current player
     * @param playerBoard the board that is associated to the current player
     * @param row         row of the piece
     * @param col         column of the piece
     * @param piece       the piece
     * @return true if able to jump false otherwise
     */
    public boolean canJump(Player player, List<Row> playerBoard, int row, int col, Piece piece) {
        boolean validPos1 = false;
        boolean validPos2 = false;
        boolean validPos3 = false;
        boolean validPos4 = false;

        /*
            201
            0P0
            403

            0 = invalid space
            1 = validPos1
            2 = validPos2
            3 = validPos3
            4 = validPos4
            P = piece being checked
        */
        if (getPiece(playerBoard, row - 1, col + 1) != null) {
            Piece pieceJump = getPiece(playerBoard, row - 1, col + 1); //get piece at validPos1
            boolean isCorrectColor = isNotPlayerColor(pieceJump, player); //check if piece at validPos1 is the opponent color
            boolean isValidSpace = isValidSpace(playerBoard, row - 2, col + 2); //Check to see if open space to jump to
            validPos1 = isValidSpace && isCorrectColor;
            if (validPos1) {
                jumpPositions.add(new int[]{row - 1, col + 1}); //if it is valid add it to possible jumps
            }
        }
        if (getPiece(playerBoard, row - 1, col - 1) != null) {
            Piece pieceJump = getPiece(playerBoard, row - 1, col - 1); //get piece at validPos2
            boolean isCorrectColor = isNotPlayerColor(pieceJump, player); //check if piece at validPos2 is the opponent color
            boolean isValidSpace = isValidSpace(playerBoard, row - 2, col - 2); //Check to see if open space to jump to
            validPos2 = isCorrectColor && isValidSpace;

            if (validPos2) {
                jumpPositions.add(new int[]{row - 1, col - 1}); //if it is valid add it to possible jumps
            }

        }

        if (piece.getType() == Piece.type.KING) { // check to see if the piece being checked is a King type
            if (getPiece(playerBoard, row + 1, col + 1) != null) {
                Piece pieceJump = getPiece(playerBoard, row + 1, col + 1); //get piece at validPos3
                boolean isCorrectColor = isNotPlayerColor(pieceJump, player); //check if piece at validPos3 is the opponent color
                boolean isValidSpace = isValidSpace(playerBoard, row + 2, col + 2); //Check to see if open space to jump to
                validPos3 = isValidSpace && isCorrectColor;
                if (validPos3) {
                    jumpPositions.add(new int[]{row + 1, col + 1}); //if it is valid add it to possible jumps
                }
            }
            if (getPiece(playerBoard, row + 1, col - 1) != null) {
                Piece pieceJump = getPiece(playerBoard, row + 1, col - 1); //get piece at validPos4
                boolean isCorrectColor = isNotPlayerColor(pieceJump, player); //check if piece at validPos4 is the opponent color
                boolean isValidSpace = isValidSpace(playerBoard, row + 2, col - 2); //Check to see if open space to jump to
                validPos4 = isValidSpace && isCorrectColor;
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
     * @param playerBoard board used for reference
     * @param row         row of the piece
     * @param col         column of the piece
     * @param piece       the piece
     * @return true if can move diagonally once false otherwise
     */
    private boolean canMove(List<Row> playerBoard, int row, int col, Piece piece) {
        boolean validPos1 = false;
        boolean validPos2 = false;
        boolean validPos3 = false;
        boolean validPos4 = false;

        /*
            201
            0P0
            403

            0 = invalid space
            1 = validPos1
            2 = validPos2
            3 = validPos3
            4 = validPos4
            P = piece being checked
        */
        if (isValidSpace(playerBoard, row - 1, col + 1)) { // check to see if validPos1 is a valid space
            validPos1 = true;
        }
        if (isValidSpace(playerBoard, row - 1, col - 1)) { // check to see if validPos2 is a valid space
            validPos2 = true;
        }

        if (piece.getType() == Piece.type.KING) { // check to see if the piece being checked is a King type
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
     * @param player the player to check
     * @return true if there are moves still available false otherwise
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
     * @return true if it should false otherwise
     */
    public boolean checkGameOver() {
        if(gameOver == true)
            return gameOver;
        if (redPlayerTotalPieces <= 0) { // check to see if there are no remaining red pieces
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
     * @return is the game over
     */
    public boolean isGameOver() {
        return gameOver;
    }

    public void resign(Player player){
        gameOver = true;
        gameOverMessage = player.getName() + " has resigned.";
        player.setPlaying(false);
    }

    /**
     * @return the game over message
     */
    public String getGameOverMessage() {
        return gameOverMessage;
    }

    @Override
    public Iterator<Row> iterator() {
        return game.getBoard().iterator();
    }
}
