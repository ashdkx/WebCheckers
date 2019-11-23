package com.webcheckers.model;

import java.util.HashMap;

/**
 * @author Nicholas Curl
 */
public class PlayerLobby{
    // list of all players signed in
    private HashMap<String, Player> players;
    // list of players waiting for a game

    public PlayerLobby(){
        this.players = new HashMap<>();
    }

    public Player getPlayer(String username){
        return players.get(username);
    }

    public synchronized   HashMap<String, Player> getPlayers() {
        return players;
    }

    public synchronized int getNumPlayers(){
        return this.players.size();
    }

    public synchronized void removePlayer(String name ){
        players.remove(name);
    }

    public synchronized void addPlayer(String username){
        Player newPlayer = new Player(username);
        this.players.put(username, newPlayer);
    }

}