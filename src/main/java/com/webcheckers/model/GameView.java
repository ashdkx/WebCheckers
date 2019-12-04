package com.webcheckers.model;


import java.util.ArrayList;
import java.util.List;


/**
 * The view of the game and modifying the pieces
 *
 * @author Nicholas Curl
 */
public class GameView {

    /**
     * Representation of the red player
     */
    private Player redPlayer;

    /**
     * Representation of the white player
     */
    private Player whitePlayer;

    /**
     * The list used to display the board
     */
    private List<Row> board;

    /**
     * The red player's board
     */
    private List<Row> redPlayerBoard = new ArrayList<>();

    /**
     * The white player's board
     */
    private List<Row> whitePlayerBoard = new ArrayList<>();

    /**
     * Constructor for a new GameView for a GameBoard
     *
     * @param redPlayer   The player to be the red player
     * @param whitePlayer The player to be the white player
     */
    public GameView(Player redPlayer, Player whitePlayer) {
        this.redPlayer = redPlayer;
        this.whitePlayer = whitePlayer;
        this.board = new ArrayList<>();
        initBoard();
        //bugFix(); //tests generic
        //testExtremeCase(); //tests extreme cases
    }

    /**
     * Gets the board to be displayed
     *
     * @return The board to be displayed
     */
    public List<Row> getBoard() {
        return board;
    }

    /**
     * Sets the board to be displayed
     *
     * @param board The board to be displayed
     */
    public void setBoard(List<Row> board) {
        this.board = board;
    }

    /**
     * Initializes both the red and white players' boards
     */
    private void initBoard() {
        initRedPlayer();
        initWhitePlayer();
    }

    /**
     * Initializes the red player's board
     * <br>
     * <pre>
     *             0   1   2   3   4   5   6   7<br>
     *         0   *   W   *   W   *   W   *   W<br>
     *         1   W   *   W   *   W   *   W   *<br>
     *         2   *   W   *   W   *   W   *   W<br>
     *         3   *   *   *   *   *   *   *   *<br>
     *         4   *   *   *   *   *   *   *   *<br>
     *         5   R   *   R   *   R   *   R   *<br>
     *         6   *   R   *   R   *   R   *   R<br>
     *         7   R   *   R   *   R   *   R   *<br>
     *
     *         R = red single pieces
     *         W = white single pieces
     *         * = blank spaces
     * </pre>
     */
    private void initRedPlayer() {
        boolean valid1 = false;

        for (int i = 0; i < 8; i++) {
            if (i <= 2) {
                redPlayerBoard.add(i, new Row(i, Piece.whiteSingle, valid1));
            } else if (i >= 5) {
                redPlayerBoard.add(i, new Row(i, Piece.redSingle, valid1));
            } else {
                redPlayerBoard.add(i, new Row(i, null, valid1));
            }
            valid1 = !valid1;
        }
    }

    /**
     * Initializes the white player's board
     * <br>
     * <pre>
     *             0   1   2   3   4   5   6   7<br>
     *         0   *   R   *   R   *   R   *   R<br>
     *         1   R   *   R   *   R   *   R   *<br>
     *         2   *   R   *   R   *   R   *   R<br>
     *         3   *   *   *   *   *   *   *   *<br>
     *         4   *   *   *   *   *   *   *   *<br>
     *         5   W   *   W   *   W   *   W   *<br>
     *         6   *   W   *   W   *   W   *   W<br>
     *         7   W   *   W   *   W   *   W   *<br>
     *
     *         R = red single pieces
     *         W = white single pieces
     *         * = blank spaces
     * </pre>
     */
    private void initWhitePlayer() {
        boolean valid2 = false;

        for (int i = 0; i < 8; i++) {
            if (i <= 2) {
                whitePlayerBoard.add(i, new Row(i, Piece.redSingle, valid2));
            } else if (i >= 5) {
                whitePlayerBoard.add(i, new Row(i, Piece.whiteSingle, valid2));
            } else {
                whitePlayerBoard.add(i, new Row(i, null, valid2));
            }
            valid2 = !valid2;
        }
    }

    /**
     * Sets the display board to the white players board when true
     *
     * @param whiteBoard If the display board should be the white player's board
     */
    public void isWhitePlayerBoard(boolean whiteBoard) {
        if (whiteBoard) {

            this.board = whitePlayerBoard;
        } else {
            this.board = redPlayerBoard;
        }
    }

    /**
     * Updates the white player's board with the pieces from the red player board
     */
    public void updateWhitePlayer() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                whitePlayerBoard.get(i).getSpace(7 - j).setPiece(redPlayerBoard.get(7 - i).getSpace(j).getPiece());
            }
        }
    }

    /**
     * Updates the red player's board with the pieces from the white player board
     */
    public void updateRedPlayer() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                redPlayerBoard.get(i).getSpace(7 - j).setPiece(whitePlayerBoard.get(7 - i).getSpace(j).getPiece());
            }
        }
    }

    /**
     * Gets the red player's board
     *
     * @return The red player's board
     */
    public List<Row> getRedPlayerBoard() {
        return redPlayerBoard;
    }

    /**
     * Gets the white player's board
     *
     * @return The white player's board
     */
    public List<Row> getWhitePlayerBoard() {
        return whitePlayerBoard;
    }

    /**
     * Gets the red player
     *
     * @return The red player
     */
    public Player getRedPlayer() {
        return redPlayer;
    }

    /**
     * Gets the white player
     *
     * @return The white player
     */
    public Player getWhitePlayer() {
        return whitePlayer;
    }

    /**
     * A testing function to help with double jumps
     */
    private void testDoubleJumpSingle() {
        clearBoard();
        redPlayerBoard.get(5).getSpace(2).setPiece(Piece.redSingle);
        redPlayerBoard.get(4).getSpace(3).setPiece(Piece.whiteSingle);
        redPlayerBoard.get(2).getSpace(3).setPiece(Piece.whiteSingle);
        redPlayerBoard.get(2).getSpace(5).setPiece(Piece.whiteSingle);
        updateWhitePlayer();

    }

    /**
     * A testing function to help with game over states
     */
    private void testGameOver() {
        clearBoard();
        redPlayerBoard.get(5).getSpace(2).setPiece(Piece.redSingle);
        redPlayerBoard.get(4).getSpace(3).setPiece(Piece.whiteSingle);
        updateWhitePlayer();
    }

    /**
     * Clears the board
     */
    public void clearBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                redPlayerBoard.get(i).getSpace(j).setPiece(null);
            }
        }
    }

    /**
     * An extreme case
     */
    private void testExtremeCase(){
        clearBoard();
        redPlayerBoard.get(2).getSpace(1).setPiece(Piece.redSingle);
        redPlayerBoard.get(1).getSpace(2).setPiece(Piece.whiteSingle);
        redPlayerBoard.get(1).getSpace(4).setPiece(Piece.whiteSingle);
        updateWhitePlayer();
    }


    /**
     * Generic bug fixing
     */
    private void bugFix(){
        clearBoard();
        redPlayerBoard.get(0).getSpace(3).setPiece(Piece.whiteSingle);
        redPlayerBoard.get(0).getSpace(5).setPiece(Piece.whiteSingle);
        redPlayerBoard.get(0).getSpace(7).setPiece(Piece.whiteSingle);
        redPlayerBoard.get(0).getSpace(1).setPiece(Piece.redKing);
        redPlayerBoard.get(2).getSpace(1).setPiece(Piece.redSingle);
        redPlayerBoard.get(2).getSpace(3).setPiece(Piece.whiteSingle);
        redPlayerBoard.get(2).getSpace(5).setPiece(Piece.whiteSingle);
        redPlayerBoard.get(2).getSpace(7).setPiece(Piece.whiteSingle);
        redPlayerBoard.get(3).getSpace(6).setPiece(Piece.whiteSingle);
        redPlayerBoard.get(4).getSpace(7).setPiece(Piece.redSingle);
        redPlayerBoard.get(5).getSpace(0).setPiece(Piece.redSingle);
        redPlayerBoard.get(5).getSpace(2).setPiece(Piece.whiteSingle);
        redPlayerBoard.get(5).getSpace(6).setPiece(Piece.redSingle);
        redPlayerBoard.get(6).getSpace(3).setPiece(Piece.whiteSingle);
        redPlayerBoard.get(6).getSpace(7).setPiece(Piece.redSingle);
        redPlayerBoard.get(7).getSpace(0).setPiece(Piece.redSingle);
        redPlayerBoard.get(7).getSpace(4).setPiece(Piece.redSingle);
        redPlayerBoard.get(7).getSpace(6).setPiece(Piece.redSingle);
        updateWhitePlayer();
    }
}