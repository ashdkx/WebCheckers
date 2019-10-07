package com.webcheckers.appl;

import com.webcheckers.model.PlayerLobby;

public class GameCenter{
    private PlayerLobby lobby;

    public GameCenter(PlayerLobby lobby){
        this.lobby = lobby;
    }

    public void addPlayer(String username){
        this.lobby.addPlayer(username);
    }
}