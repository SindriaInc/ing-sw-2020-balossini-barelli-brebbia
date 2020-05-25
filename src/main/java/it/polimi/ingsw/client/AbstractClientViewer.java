package it.polimi.ingsw.client;

import it.polimi.ingsw.client.clientstates.*;

/**
 * This is the core of the final user-interactive application
 * Each view should call interactive methods in it's related state and display information available in the state
 */
public abstract class AbstractClientViewer {

    /**
     * Shows the initial connection view, asking for ip and port
     * @param state The input state
     */
    public abstract void viewInput(InputState state);

    /**
     * Shows the login view, asking for name and age
     * @param state The login state
     */
    public abstract void viewLogin(LoginState state);

    /**
     * Shows the lobby view, listing existing rooms and asking to join or create a room
     * @param state The lobby state
     */
    public abstract void viewLobby(LobbyState state);

    /**
     * Shows the room view, listing players in the room and waiting for the room to be full
     * @param state The room state
     */
    public abstract void viewRoom(RoomState state);

    /**
     * Shows the game view, displaying the board and waiting for user interaction depending on server requests
     * @param state The game state
     */
    public abstract void viewGame(GameState state);

    /**
     * Shows the game end, displaying the winner
     * @param state The end state
     */
    public abstract void viewEnd(EndState state);

}
