package it.polimi.ingsw.client.clientstates.lobbystates;

import java.util.Scanner;

public class GetNewLobbyGameType extends AbstractLobbyState {

    @Override
    public boolean readGameType() {
        Scanner type = new Scanner(System.in);
        return type.nextBoolean();
    }

    @Override
    public AbstractLobbyState nextLobbyState() {
        return new CreateLobby();
    }

}
