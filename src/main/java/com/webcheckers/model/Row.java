package com.webcheckers.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author Nicholas Curl
 */
public class Row implements Iterable<Space>{


    private int index;
    private List<Space> space;

    public Row(int index, Piece piece, boolean startValid){
        this.index = index;
        this.space = new ArrayList<>();
        boolean valid = startValid;

        for(int i = 0; i<8;i++){
            if (valid){
                space.add(new Space(i,piece,true));
            }
            else{
                space.add(new Space(i,null,false));
            }
            valid = !valid;
        }

    }

    @Override
    public Iterator<Space> iterator(){
        return space.iterator();
    }

    public int getIndex(){
        return index;
    }

}
