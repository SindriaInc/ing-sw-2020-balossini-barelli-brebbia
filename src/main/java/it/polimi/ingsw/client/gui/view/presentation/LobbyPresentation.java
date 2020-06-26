package it.polimi.ingsw.client.gui.view.presentation;

import it.polimi.ingsw.client.gui.GuiAssets;
import it.polimi.ingsw.client.gui.GuiConstants;
import it.polimi.ingsw.client.gui.view.component.RoomBox;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.List;

public class LobbyPresentation extends AbstractPresentation {

    public LobbyPresentation(GuiAssets assets) {
        super(assets);
    }


    public Pane generatePresentation(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height,
                                     List<RoomBox> rooms,
                                     Button action
    ) {
        style(action);

        GridPane pane = new GridPane();

        ImageView topImage = new ImageView(getAssets().getImage(GuiAssets.Images.LOBBY_TOP));
        topImage.setPreserveRatio(false);
        pane.add(topImage, 0, 0);

        ImageView background = new ImageView(getAssets().getImage(GuiAssets.Images.LOBBY_BACKGROUND));
        background.setPreserveRatio(false);
        background.fitWidthProperty().bind(width);
        StackPane center = new StackPane(background);

        if (rooms.size() <= 0) {
            Text noRoomsText = new Text("No rooms found, please create a new one\n\nYou can also wait for other players to create a room");
            style(noRoomsText);
            center.getChildren().add(noRoomsText);
        } else {
            // The vertical arrangement of room components
            VBox roomsContainer = new VBox();
            Text lobbyText = new Text("Click [Join] to enter one of the following rooms");
            styleTitle(lobbyText);
            VBox.setMargin(lobbyText, new Insets(GuiConstants.DEFAULT_SPACING));
            roomsContainer.getChildren().add(lobbyText);
            roomsContainer.setBackground(Background.EMPTY);
            roomsContainer.setPadding(new Insets(GuiConstants.DEFAULT_SPACING));

            // The scrollpane over the roomsContainer
            ScrollPane scrollPane = new ScrollPane(roomsContainer);
            scrollPane.setBackground(Background.EMPTY);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

            for (RoomBox roomBox : rooms) {
                roomBox.minWidthProperty().bind(width.subtract(GuiConstants.DEFAULT_SPACING * 4));
                Color boxBackground = Color.rgb(224, 224, 224);
                roomBox.setBackground(new Background(new BackgroundFill(boxBackground, new CornerRadii(2), new Insets(GuiConstants.DEFAULT_SPACING / 2d))));
                roomBox.setPadding(new Insets(GuiConstants.DEFAULT_SPACING * 2));
                roomBox.setSpacing(GuiConstants.DEFAULT_SPACING);
                roomBox.getLabelBox().setSpacing(GuiConstants.DEFAULT_SPACING * 3);

                styleLabel(roomBox.getName());
                styleLabel(roomBox.getPlayers());
                styleLabel(roomBox.getSimple());
                style(roomBox.getButton());

                roomsContainer.getChildren().add(roomBox);
            }

            // Add an empty region to distance the scrollbar on the right
            HBox scrollpaneContainer = new HBox();
            scrollpaneContainer.getChildren().add(scrollPane);
            scrollpaneContainer.setSpacing(GuiConstants.DEFAULT_SPACING);
            scrollpaneContainer.getChildren().add(new Text(""));
            center.getChildren().add(scrollpaneContainer);
        }

        pane.add(center, 0, 1);

        ImageView bottomImage = new ImageView(getAssets().getImage(GuiAssets.Images.LOBBY_BOTTOM));
        bottomImage.setPreserveRatio(false);

        background.fitHeightProperty().bind(Bindings.createDoubleBinding(() -> {
            double topHeight = topImage.getImage().getHeight();
            double bottomHeight = bottomImage.getImage().getHeight();
            return Math.max(GuiConstants.DEFAULT_SPACING, height.get() - (topHeight + bottomHeight));
        }, width, height));

        StackPane bottomPane = new StackPane(bottomImage, action);
        pane.add(bottomPane, 0, 2);

        GridPane.setVgrow(center, Priority.ALWAYS);
        pane.setPadding(new Insets(0, 0, 0, 0));

        topImage.fitWidthProperty().bind(width);
        bottomImage.fitWidthProperty().bind(width);
        return pane;
    }

    private void styleLabel(Label label) {
        label.setFont(getAssets().getFont());
    }

}
