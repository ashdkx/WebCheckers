package com.webcheckers.model;

import java.util.HashMap;

public class PlayerLobby{
    private HashMap<String,Player> players;
    private int playerNum = 0;

    public PlayerLobby(){
        this.players = new HashMap<>();
    }

    public Player getPlayer(String username){
        return players.get(username);
    }

    public HashMap<String, Player> getPlayers() {
        return players;
    }

    public int getNumPlayers(){
        return this.players.size();
    }

    public void addPlayer(String username){
        Player newPlayer = new Player(username, String.valueOf(playerNum));
        this.players.put(username, newPlayer);
        playerNum++;
    }
}