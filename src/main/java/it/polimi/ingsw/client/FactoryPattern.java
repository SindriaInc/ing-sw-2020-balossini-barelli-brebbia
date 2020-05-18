package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CliFunctions;
import it.polimi.ingsw.client.cli.InputHandler;
import it.polimi.ingsw.client.gui.GuiFunctions;

public class FactoryPattern {

    private final boolean gui;

    public FactoryPattern (boolean gui) {
        this.gui = gui;
    }

    public AbstractFunctions UsableFunctions(InputHandler inputHandler) {
        if (gui) {
            return new GuiFunctions();
        }
        return new CliFunctions(inputHandler);
    }

}
