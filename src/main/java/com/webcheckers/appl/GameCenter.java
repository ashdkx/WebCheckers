package com.webcheckers.appl;

import com.webcheckers.model.Player;

import java.util.HashMap;

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

    public void removePlayer(String username){
        lobby.getPlayers().remove(username);
    }
}