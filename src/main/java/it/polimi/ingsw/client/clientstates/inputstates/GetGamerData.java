package it.polimi.ingsw.client.clientstates.inputstates;

import it.polimi.ingsw.client.clientstates.AbstractClientState;

import java.util.Scanner;

public class GetGamerData extends AbstractInputState {

    @Override
    public String readName() {
        Scanner name = new Scanner(System.in);
        return name.next();
    }

    @Override
    public int readAge() {
        Scanner age = new Scanner(System.in);
        return age.nextInt();
    }

    @Override
    public AbstractInputState nextInputState() {
        return new GetConnectionData();
    }
}
