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

public abstract class AbstractPreGamePresentation extends AbstractPresentation {

    public AbstractPreGamePresentation(GuiAssets assets) {
        super(assets);
    }

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
