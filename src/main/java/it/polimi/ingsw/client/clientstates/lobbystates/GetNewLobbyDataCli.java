package it.polimi.ingsw.client.clientstates.lobbystates;

import it.polimi.ingsw.client.clientstates.DataTypes;

import java.util.Scanner;

public class GetNewLobbyDataCli extends GetNewLobbyData {

    @Override
    public DataTypes.LobbyData readLobbyData() {
        DataTypes.LobbyData lobbyData = new DataTypes.LobbyData();

        Scanner lobby = new Scanner(System.in);

        lobbyData.setType(lobby.nextBoolean());
        lobbyData.setPlayerNumber(lobby.nextInt());

        return lobbyData;
    }

    @Override
    public AbstractLobbyState nextLobbyState() {
        return new CreateLobby();
    }

}
