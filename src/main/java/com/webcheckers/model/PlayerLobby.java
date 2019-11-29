package com.webcheckers.model;

import java.util.HashMap;

/**
 * The controller that stores all players
 *
 * @author Nicholas Curl
 */
public class PlayerLobby{
    /**
     * Map of all players signed in
     */
    private HashMap<String, Player> players;

    /**
     * Representation of a lobby to store all of the players
     */
    public PlayerLobby(){
        this.players = new HashMap<>();
    }

    /**
     * Gets the player based on the username
     *
     * @param username The username of the player to get
     * @return The player with the specified username
     */
    public Player getPlayer(String username){
        return players.get(username);
    }

    /**
     * Gets the map of the stored players
     *
     * @return The map of the stored players
     */
    public synchronized   HashMap<String, Player> getPlayers() {
        return players;
    }

    /**
     * Gets the number of players in the lobby
     *
     * @return The number of players in the lobby
     */
    public synchronized int getNumPlayers(){
        return this.players.size();
    }


    /**
     * Removes a player from the lobby with specified username
     *
     * @param name The username of the player to remove
     */
    public synchronized void removePlayer(String name ){
        players.remove(name);
    }

    /**
     * Adds a player into the player lobby with the specified username
     *
     * @param username The username of the new player
     */
    public synchronized void addPlayer(String username){
        Player newPlayer = new Player(username);
        this.players.put(username, newPlayer);
    }

}