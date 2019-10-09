package com.webcheckers.appl;

import com.webcheckers.model.Player;

import java.util.HashMap;

public class PlayerLobby{
    // list of all players signed in
    private HashMap<String, Player> players;
    // list of players waiting for a game
    private HashMap<String,Player> playersWaiting;
    private int playerNum = 0;

    public PlayerLobby(){
        this.players = new HashMap<>();
        this.playersWaiting = new HashMap<>();
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

    public void removeWaiting(String username){
        playersWaiting.remove(username);
    }

    public synchronized void addPlayer(String username){
        Player newPlayer = new Player(username, String.valueOf(playerNum));
        this.players.put(username, newPlayer);
        this.playersWaiting.put(username, newPlayer);
        playerNum++;
    }
}