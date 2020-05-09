package it.polimi.ingsw.client;

import it.polimi.ingsw.client.clientstates.inputstates.GetGamerData;
import it.polimi.ingsw.client.clientstates.inputstates.GetGamerDataCli;
import it.polimi.ingsw.client.clientstates.inputstates.GetGamerDataGui;
import it.polimi.ingsw.client.clientstates.lobbystates.ChooseLobby;
import it.polimi.ingsw.client.clientstates.lobbystates.ChooseLobbyCli;
import it.polimi.ingsw.client.clientstates.lobbystates.ChooseLobbyGui;

public class FactoryPattern {

    private final boolean gui;

    public FactoryPattern (boolean gui) {
        this.gui = gui;
    }

    public GetGamerData getGamerData() {
        if (gui) {
            return new GetGamerDataGui();
        }

        return new GetGamerDataCli();
    }

    public ChooseLobby chooseLobby() {
        if (gui) {
            return new ChooseLobbyGui();
        }

        return new ChooseLobbyCli();
    }

}
