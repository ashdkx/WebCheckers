package com.webcheckers.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * The representation of a row of spots on the game board
 *
 * @author Nicholas Curl
 */
public class Row implements Iterable<Space>{

    /**
     * The row number
     */
    private int index;

    /**
     * The list of spaces in the row
     */
    private List<Space> space;

    /**
     * Representation of a row for the game
     *
     * @param index The row number
     * @param piece The piece to set in the row
     * @param startValid If the starting space is black
     */
    public Row(int index, Piece piece, boolean startValid){
        this.index = index;
        this.space = new ArrayList<>();
        boolean valid = startValid;
        for(int i = 0; i<8;i++){ // create new spaces to fill the row alternating between black and white spaces
            if (valid){
                space.add(new Space(i,piece,true));
            }
            else{
                space.add(new Space(i,null,false));
            }
            valid = !valid;
        }

    }

    /**
     * The iterator of the spaces in the row
     *
     * @return Iterator of the space in the row
     */
    @Override
    public Iterator<Space> iterator(){
        return space.iterator();
    }

    /**
     * Get the row number
     *
     * @return The row number
     */
    public int getIndex(){
        return index;
    }


    /**
     * Get the space within the row
     *
     * @param col The column of the space in the row
     * @return The space at the specified column
     */
    public Space getSpace(int col){
        return space.get(col);
    }

}
