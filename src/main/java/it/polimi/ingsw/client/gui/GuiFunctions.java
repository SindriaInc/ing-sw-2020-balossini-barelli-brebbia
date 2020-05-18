package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.AbstractFunctions;
import it.polimi.ingsw.client.ClientData;
import it.polimi.ingsw.client.clientstates.AADataTypes;

import java.util.List;

public class GuiFunctions extends AbstractFunctions {

    @Override
    public String in(String request) {
        return null;
    }

    @Override
    public int inInt(String request) {
        return 0;
    }

    @Override
    public void out(String message) {}

    @Override
    public void printLobby(ClientData clientData) {

    }

    @Override
    public void printRoom(ClientData clientData) {

    }

    @Override
    public void printBoard(ClientData clientData, AADataTypes.InGame inGame, List<AADataTypes.Command> availableCommands) {

    }

    @Override
    public void printEnd(ClientData clientData) {

    }
}
