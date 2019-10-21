package com.webcheckers.appl;

import com.webcheckers.model.Player;
import com.webcheckers.model.PlayerLobby;

import java.util.HashMap;

/**
 * @author Nicholas Curl
 */
public class GameCenter{
    private PlayerLobby lobby;
    private Player currentUser = null;

    public GameCenter(){
        this.lobby = new PlayerLobby();
    }

    public void addPlayer(String username){
        this.lobby.addPlayer(username);
    }

     public HashMap<String, Player> getPlayers() {
        return lobby.getPlayers();
    }

    public void removePlayer(String name ){
        lobby.removePlayer(name);
    }

    public Player getPlayer(String username){
        return lobby.getPlayer(username);
    }


    public void setPlayer1(Player player, boolean status){
        lobby.setPlayer1(player,status);
    }


    public void setPlaying(Player player, boolean status){
        lobby.setPlaying(player,status);
    }


    public void setGame(Player player, GameBoard game){
        lobby.setGame(player,game);
    }

    public GameBoard getGame(Player player){
        return lobby.getGame(player);
    }
}