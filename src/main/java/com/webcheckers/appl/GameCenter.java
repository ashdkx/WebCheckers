package com.webcheckers.appl;

import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import com.webcheckers.model.PlayerLobby;

import java.util.HashMap;

/**
 * @author Nicholas Curl
 */
public class GameCenter{
    private PlayerLobby lobby;

    public GameCenter(){
        this.lobby = new PlayerLobby();
    }

    public void addPlayer(String username){
        lobby.addPlayer(username);
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


}