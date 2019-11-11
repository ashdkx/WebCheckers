package com.webcheckers.model;

import com.webcheckers.model.Player;

import com.webcheckers.appl.GameBoard;

import java.util.HashMap;

/**
 * @author Nicholas Curl
 */
public class PlayerLobby{
    // list of all players signed in
    private HashMap<String, Player> players;
    // list of players waiting for a game
    private int playerNum = 0;

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
        Player newPlayer = new Player(username, String.valueOf(playerNum));
        this.players.put(username, newPlayer);
        playerNum++;
    }

}