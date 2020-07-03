package it.polimi.ingsw.client.gui.view.presentation;

import it.polimi.ingsw.client.gui.GuiAssets;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class GodsPresentation extends AbstractPreGamePresentation {

    /**
     * Class constructor, set the assets
     *
     * @param assets The assets
     */
    public GodsPresentation(GuiAssets assets) {
        super(assets);
    }

    /**
     * Generate a presentation with the given data
     * @param width The width
     * @param height The height
     * @param center The center stack pane
     * @param bottom The bottom stack pane
     * @return A pane with generated with the previous parts
     */
    public Pane generatePresentation(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height,
                                      StackPane center, StackPane bottom
    ) {
        return super.generatePresentation(width, height, center, bottom);
    }

}
