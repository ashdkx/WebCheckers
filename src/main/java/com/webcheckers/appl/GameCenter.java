package com.webcheckers.appl;

import com.webcheckers.model.PlayerLobby;

public class GameCenter{
    private PlayerLobby lobby;

    public GameCenter(){
        this.lobby = new PlayerLobby();
    }

    public void addPlayer(String username){
        this.lobby.addPlayer(username);
    }
}