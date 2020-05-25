package it.polimi.ingsw.client.gui.view;

import it.polimi.ingsw.client.clientstates.AbstractClientState;
import it.polimi.ingsw.model.gamestates.AbstractGameState;
import javafx.scene.Parent;
import javafx.scene.Scene;

public abstract class AbstractGuiView {

    /**
     * Builds the view
     * @return the output of the view, as a JavaFX root node
     */
    public abstract Parent generateView();

    /**
     * Obtain the state used by the view
     * @return the state
     */
    public abstract AbstractClientState getState();

}
