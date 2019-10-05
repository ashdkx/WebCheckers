package com.webcheckers.model;

import java.util.HashMap;

public class PlayerLobby{
    private HashMap<String,Player> players;

    public PlayerLobby(){
        this.players = new HashMap<>();
    }

    public Player getPlayer(String sessionID){
        return players.get(sessionID);
    }

    public int getNumPlayers(){
        return this.players.size();
    }

    public void addPlayer(String username, String sessionID){
        Player newPlayer = new Player(username, sessionID);
        this.players.put(sessionID, newPlayer);
    }
}