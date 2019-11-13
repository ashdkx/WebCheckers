package com.webcheckers.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * unit test for PlayerLobby class
 * @author - Alec Jackson
 */

public class PlayerLobbyTest {
    private static final String SAMPLE_NAME = "Bob";
    private static final int ONE_ADDED = 1;
    private static final int EMPTY = 0;

    /*
     * tests if player added
     */
    @Test
    public void ifPlayerAdded(){
        PlayerLobby lobby = new PlayerLobby();
        lobby.addPlayer(SAMPLE_NAME);
        assertEquals(lobby.getNumPlayers(),ONE_ADDED,"Number of players added is wrong.");
        assertEquals(lobby.getPlayer(SAMPLE_NAME).getName(),SAMPLE_NAME,"Player was not added.");
    }

    /*
     * tests if player removed
     */
    @Test
    public void ifPlayerRemoved(){
        PlayerLobby lobby = new PlayerLobby();
        lobby.addPlayer(SAMPLE_NAME);
        lobby.removePlayer(SAMPLE_NAME);
        assertEquals(lobby.getPlayer(SAMPLE_NAME),null,"Player not removed.");

    }

}
