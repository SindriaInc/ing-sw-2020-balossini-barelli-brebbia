package it.polimi.ingsw.client.gui.view.presentation;

import it.polimi.ingsw.client.gui.GuiAssets;
import it.polimi.ingsw.client.gui.GuiConstants;
import it.polimi.ingsw.client.gui.view.component.BoardBox;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.List;

/**
 * The board presentation
 */
public class BoardPresentation extends AbstractPresentation {

    /**
     * Class constructor, set assets
     * @param assets The assets
     */
    public BoardPresentation(GuiAssets assets) {
        super(assets);
    }

    /**
     * Generate a board presentation
     * @param width The width
     * @param height The height
     * @param boardPane The board pane
     * @param infoText The info text
     * @param infoButtons The info buttons
     * @return The board presentation
     */
    public Pane generatePresentation(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height, BoardBox boardPane, Text infoText, List<Button> infoButtons) {
        ImageView boardBackground = new ImageView(getAssets().getImage(GuiAssets.Images.GAME_BOARD));
        StackPane boardContainer = new StackPane(boardBackground, boardPane);

        boardPane.setAlignment(Pos.CENTER);

        GridPane infoPane = new GridPane();

        infoPane.add(infoText, 0, 0);
        GridPane.setHalignment(infoText, HPos.CENTER);
        infoText.setTextAlignment(TextAlignment.CENTER);
        infoText.wrappingWidthProperty().bind(infoPane.maxWidthProperty().subtract(GuiConstants.DEFAULT_SPACING * 5));
        int index = 1;
        for (Button button : infoButtons) {
            infoPane.add(button, 0, index++);
            button.setAlignment(Pos.CENTER);
            GridPane.setHalignment(button, HPos.CENTER);
            styleButton(button);
            button.setWrapText(true);
        }

        infoPane.setVgap(GuiConstants.DEFAULT_SPACING);
        infoPane.setMinWidth(GuiConstants.INFO_MIN_SIZE);
        infoPane.setAlignment(Pos.CENTER);
        infoPane.setPadding(new Insets(0, 0, 0, GuiConstants.DEFAULT_SPACING));

        ImageView infoPaneBackground = new ImageView(getAssets().getImage(GuiAssets.Images.GAME_SIDE));
        StackPane infoPaneContainer = new StackPane(infoPaneBackground, infoPane);

        infoPaneBackground.fitHeightProperty().bind(height);
        infoPane.maxWidthProperty().bind(infoPaneBackground.getImage().widthProperty());

        Pane fillLeft = new Pane();
        Pane fillRight = new Pane();

        HBox pane = new HBox();
        pane.setSpacing(GuiConstants.DEFAULT_SPACING);
        pane.getChildren().add(fillLeft);
        pane.getChildren().add(boardContainer);
        pane.getChildren().add(fillRight);
        pane.getChildren().add(infoPaneContainer);
        pane.setFillHeight(true);
        HBox.setHgrow(fillLeft, Priority.ALWAYS);
        HBox.setHgrow(fillRight, Priority.ALWAYS);

        styleTitle(infoText);

        ImageView background = new ImageView(getAssets().getImage(GuiAssets.Images.GAME_BACKGROUND));
        StackPane root = new StackPane(background, pane);
        bindCover(width, height, background);

        return root;
    }


}
