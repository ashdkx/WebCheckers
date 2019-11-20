package com.webcheckers.appl;

import com.webcheckers.model.MoveSave;
import com.webcheckers.model.Player;
import com.webcheckers.model.PlayerLobby;
import com.webcheckers.model.SavedGame;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Nicholas Curl
 */
public class GameCenter{
    private PlayerLobby lobby;

    private Map<String, GameBoard> games = new HashMap<>(); //Map to store all of the games based on a unique identifier
    private Map<String, SavedGame> savedGames = new HashMap<>();


    /**
     * Constructor of the game center
     */
    public GameCenter(){
        this.lobby = new PlayerLobby();
    }

    /**
     * Adds player with username username to the lobby
     * @param username the string identifier of the player to be added
     */
    public void addPlayer(String username){
        lobby.addPlayer(username);
    }

    /**
     * Gets all the players present in the lobby
     * @return the map of all the players in the lobby
     */
     public HashMap<String, Player> getPlayers() {
        return lobby.getPlayers();
    }

    /**
     * Removes the player with the username name from the lobby
     * @param name the string identifier of the player to remove
     */
    public void removePlayer(String name ){
        lobby.removePlayer(name);
    }

    /**
     * Gets the player based on the username from the lobby
     * @param username the string identifier of the player
     * @return the player associated with the username from the lobby
     */
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
        board.addMove();
    }

    public void addGameSave(String gameID){
        GameBoard board = games.get(gameID);
        SavedGame gameSave = new SavedGame(board.getMoves(),board.getRedPlayer(),board.getWhitePlayer());
        savedGames.put(gameID,gameSave);
    }

    public SavedGame getGameSave(String gameID){
        return savedGames.get(gameID);
    }

    public Map<String, SavedGame> getSavedGames() {
        return savedGames;
    }
}