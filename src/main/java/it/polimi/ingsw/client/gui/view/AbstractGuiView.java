package it.polimi.ingsw.client.gui.view;

import javafx.scene.Scene;

public abstract class AbstractGuiView {

    /**
     * Builds the view
     * @return the output of the view, as a JavaFX scene
     */
    public abstract Scene generateView();

}
