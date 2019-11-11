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

    /**
     * Get the GameBoard based on the gameID
     * @param gameId the unique identifier for the game
     * @return the GameBoard based on the gameID
     */
    public GameBoard getGame(String gameId){

        return games.get(gameId);

    }

    /**
     * Gets all the games
     * @return the entire map of every game
     */
    public Map<String, GameBoard> getGames() {
        return games;
    }


    /**
     * Creates a new game and adds it to the games
     * @param gameId a unique string identifier
     * @param redPlayer player going to be the red player
     * @param whitePlayer player going to be the white player
     */
    public void addNewGame(String gameId, Player redPlayer, Player whitePlayer){
        GameBoard board = new GameBoard(redPlayer,whitePlayer); //create a new GameBoard
        games.put(gameId,board); //store it based on the gameId into a map
        redPlayer.setPlaying(true); //set the redPlayer to be playing
        whitePlayer.setPlaying(true); //set the whitePlayer to be playing
        board.setPlayerTurn(redPlayer); //set that its the redPlayer's turn
    }

}