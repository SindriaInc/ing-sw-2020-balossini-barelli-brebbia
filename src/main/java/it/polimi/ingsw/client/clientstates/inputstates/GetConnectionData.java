package it.polimi.ingsw.client.clientstates.inputstates;

import it.polimi.ingsw.client.clientstates.AbstractClientState;
import it.polimi.ingsw.client.clientstates.lobbystates.ChooseLobby;

import java.util.Scanner;

public class GetConnectionData extends AbstractInputState {

    @Override
    public String readIP() {
        Scanner ip = new Scanner(System.in);
        return ip.next();
    }

    @Override
    public int readPort() {
        Scanner port = new Scanner(System.in);
        return port.nextInt();
    }

    @Override
    public AbstractInputState nextInputState() {
        return null;
    }
}
