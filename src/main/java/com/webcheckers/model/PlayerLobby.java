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

    public synchronized void setPlayer1(Player player, boolean status){
        player.setPlayer1(status);
    }

    public synchronized boolean isPlayer1(Player player){
        return player.isPlayer1();
    }

    public synchronized void setPlaying(Player player, boolean status){
        player.setPlaying(status);
    }

    public synchronized boolean isPlaying(Player player){
        return player.isPlaying();
    }

    public synchronized void setGame(Player player, GameBoard game){
        player.setGame(game);
    }

    public synchronized GameBoard getGame(Player player){
        return player.getGame();
    }

    public synchronized void setPlayerColor(Player player, GameBoard.color color){
        player.setColor(color);
    }

    public synchronized GameBoard.color getPlayerColor(Player player){
        return player.getColor();
    }

    public synchronized void setPlayerTurn(Player player, boolean playerTurn){
        player.setMyTurn(playerTurn);
    }

    public synchronized boolean isPlayerTurn(Player player){
        return player.isMyTurn();
    }

}