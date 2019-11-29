package com.webcheckers.model;

/**
 * The data structure for a piece's position to be used by Gson
 *
 * @author Nicholas Curl
 */
public class Position {

    /**
     * The row number
     */
    private int row;

    /**
     * The cell number
     */
    private int cell;


    /**
     * Creates a new position with a specified row and cell
     *
     * @param row The row number
     * @param cell The cell number
     */
    public Position(int row, int cell){
        this.row = row;
        this.cell = cell;
    }

    /**
     * Get the cell number
     *
     * @return The cell number
     */
    public int getCell() {
        return cell;
    }

    /**
     * Get the row number
     *
     * @return The row number
     */
    public int getRow() {
        return row;
    }
}
