package it.polimi.ingsw.client.gui.view;

import it.polimi.ingsw.client.clientstates.AbstractClientState;
import it.polimi.ingsw.client.gui.GuiAssets;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Parent;

public abstract class AbstractGuiView {

    private final GuiAssets assets;

    public AbstractGuiView(GuiAssets assets) {
        this.assets = assets;
    }

    /**
     * Builds the view
     * @param width The parent's width property
     * @param height The parent's height property
     * @return the output of the view, as a JavaFX root node
     */
    public abstract Parent generateView(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height);

    /**
     * Obtain the state used by the view
     * @return the state
     */
    public abstract AbstractClientState getState();

    public GuiAssets getAssets() {
        return assets;
    }

}
