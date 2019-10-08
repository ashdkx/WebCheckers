package com.webcheckers.appl;

import com.webcheckers.model.Player;
import com.webcheckers.model.PlayerLobby;

import java.util.HashMap;

public class GameCenter{
    private PlayerLobby lobby;

    public GameCenter(){
        this.lobby = new PlayerLobby();
    }

    public void addPlayer(String username){
        this.lobby.addPlayer(username);
    }

     public HashMap<String, Player> getPlayers() {
        return lobby.getPlayers();
    }

    public Player getPlayer(String username){
        return lobby.getPlayer(username);
    }
}