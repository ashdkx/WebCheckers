package com.webcheckers.model;

import java.util.HashMap;

public class PlayerLobby{
    private HashMap<String,Player> players;
    private int playerNum = 0;

    public PlayerLobby(){
        this.players = new HashMap<>();
    }

    public Player getPlayer(int playerNum){
        return players.get(String.valueOf(playerNum));
    }

    public HashMap<String, Player> getPlayers() {
        return players;
    }

    public int getNumPlayers(){
        return this.players.size();
    }

    public void addPlayer(String username){
        Player newPlayer = new Player(username, String.valueOf(playerNum));
        this.players.put(String.valueOf(playerNum), newPlayer);
        playerNum++;
    }
}