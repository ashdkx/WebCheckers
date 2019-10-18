package com.webcheckers.model;

import com.google.gson.Gson;

public class Move {

    private Position start;
    private Position end;

    public Move(){

    }

    public Position getStart() {
        return start;
    }

    public Position getEnd() {
        return end;
    }

    @Override

    public String toString(){
        return "Move:{\"start\"={\"row\"="+this.getStart().getRow()+",\"cell\"="+this.getStart().getCell()+"}, \"end\"={\"row\"="+this.getEnd().getRow()+",\"cell\"="+this.getEnd().getCell()+"}}";
    }

}
