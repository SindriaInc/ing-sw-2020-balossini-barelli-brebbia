package it.polimi.ingsw.client;

import it.polimi.ingsw.client.clientstates.AADataTypes;
import it.polimi.ingsw.client.message.ErrorMessage;
import it.polimi.ingsw.common.event.AbstractEvent;
import it.polimi.ingsw.common.logging.Logger;
import it.polimi.ingsw.common.serializer.GsonEventSerializer;
import it.polimi.ingsw.common.serializer.SerializationException;

import java.util.List;

public abstract class AbstractFunctions {

    /**
     * Print a request and read the following String type input
     * @param request
     * @return The input
     */
    public abstract String in(String request);

    /**
     * Print a request and read the following int type input
     * @param request
     * @return The input
     */
    public abstract int inInt(String request);

    /**
     * Print the message
     * @param message The message
     */
    public abstract void out(String message);

    /**
     * Print the lobby
     * @param clientData The data of the game
     */
    public abstract void printLobby(ClientData clientData);

    /**
     * Print the room
     * @param clientData The data of the game
     */
    public abstract void printRoom(ClientData clientData);

    /**
     * Print the board
     * @param clientData The data of the game
     * @param inGame True when the player is still in game
     */
    public abstract void printBoard(ClientData clientData, AADataTypes.InGame inGame, List<AADataTypes.Command> availableCommands);

    /**
     * Print the end screen
     * @param clientData The data of the game
     */
    public abstract void printEnd(ClientData clientData);

}
