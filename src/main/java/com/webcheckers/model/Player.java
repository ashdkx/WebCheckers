package com.webcheckers.model;

public class Player{
    private String name;
    private String sessionID;

    public Player(String name, String sessionID) {
        this.name = name;
        this.sessionID = sessionID;
    }

    public String getName() {
        return name;
    }

    public String getSessionID(){
        return sessionID;
    }

    public boolean equals(Player player){
        if(this.name.equals(player.name)){
            return true;
        }
        return false;
    }
}