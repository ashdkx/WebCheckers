package com.webcheckers.appl;

import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import com.webcheckers.model.PlayerLobby;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Nicholas Curl
 */
public class GameCenter{
    private PlayerLobby lobby;

    private Map<String, GameBoard> games = new HashMap<>();

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

    public GameBoard getGame(String gameId){

        return games.get(gameId);

    }

    public Map<String, GameBoard> getGames() {
        return games;
    }

    public void addNewGame(String gameId, Player redPlayer, Player whitePlayer){
        GameBoard board = new GameBoard(redPlayer,whitePlayer);
        games.put(gameId,board);
        redPlayer.setPlaying(true);
        whitePlayer.setPlaying(true);
        board.setPlayerTurn(redPlayer);
    }

}