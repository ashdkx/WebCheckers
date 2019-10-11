package com.webcheckers.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Row implements Iterable<Space>{


    private int index;
    private List<Space> space;

    public Row(int index, boolean startValid){
        this.index = index;
        this.space = new ArrayList<>();
        boolean valid = startValid;
        for(int i = 0; i<8;i++){
            space.add(new Space(i,new Piece("standard","red"),valid));
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
