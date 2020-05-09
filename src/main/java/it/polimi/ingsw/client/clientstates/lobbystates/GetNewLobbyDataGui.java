package it.polimi.ingsw.client.clientstates.lobbystates;

import it.polimi.ingsw.client.clientstates.DataTypes;

import java.util.Scanner;

public class GetNewLobbyDataGui extends GetNewLobbyData {

    @Override
    public DataTypes.LobbyData readLobbyData() {
        return super.readLobbyData();
    }

    @Override
    public AbstractLobbyState nextLobbyState() {
        return new CreateLobby();
    }

}
