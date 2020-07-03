package it.polimi.ingsw.client.gui.view.presentation;

import it.polimi.ingsw.client.gui.GuiAssets;
import it.polimi.ingsw.client.gui.GuiConstants;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

/**
 * Presentation of a lobby/room state
 */
public abstract class AbstractPreGamePresentation extends AbstractPresentation {

    /**
     * Class constructor, set the assets
     * @param assets The assets
     */
    public AbstractPreGamePresentation(GuiAssets assets) {
        super(assets);
    }

    /**
     * Generate a presentation given width, height, center and bottom stack panes
     * @param width The width
     * @param height The height
     * @param center The center stack pane
     * @param bottom The bottom stack pane
     * @return A completed pane created with given arguments
     */
    protected Pane generatePresentation(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height,
                                        StackPane center, StackPane bottom
    ) {
        GridPane pane = new GridPane();

        ImageView topImage = new ImageView(getAssets().getImage(GuiAssets.Images.LOBBY_TOP));
        topImage.setPreserveRatio(false);
        pane.add(topImage, 0, 0);

        ImageView background = new ImageView(getAssets().getImage(GuiAssets.Images.LOBBY_BACKGROUND));
        background.setPreserveRatio(false);
        background.fitWidthProperty().bind(width);
        center.getChildren().add(0, background);
        pane.add(center, 0, 1);

        ImageView bottomImage = new ImageView(getAssets().getImage(GuiAssets.Images.LOBBY_BOTTOM));
        bottomImage.setPreserveRatio(false);

        background.fitHeightProperty().bind(Bindings.createDoubleBinding(() -> {
            double topHeight = topImage.getImage().getHeight();
            double bottomHeight = bottomImage.getImage().getHeight();
            return Math.max(GuiConstants.DEFAULT_SPACING, height.get() - (topHeight + bottomHeight));
        }, width, height));

        bottom.getChildren().add(0, bottomImage);
        pane.add(bottom, 0, 2);

        GridPane.setVgrow(center, Priority.ALWAYS);
        pane.setPadding(new Insets(0, 0, 0, 0));

        topImage.fitWidthProperty().bind(width);
        bottomImage.fitWidthProperty().bind(width);
        return pane;
    }

}
