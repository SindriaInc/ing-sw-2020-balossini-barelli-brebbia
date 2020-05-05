package it.polimi.ingsw.client.clientstates.lobbystates;

import java.util.Scanner;

public class GetNewLobbyPlayerNumber extends AbstractLobbyState {

    @Override
    public int readPlayerNumber() {
        Scanner number = new Scanner(System.in);
        return number.nextInt();
    }

    @Override
    public AbstractLobbyState nextLobbyState() {
        return new GetNewLobbyGameType();
    }

}
