package it.polimi.ingsw.client.gui.view.presentation;

import it.polimi.ingsw.client.gui.GuiAssets;
import it.polimi.ingsw.client.gui.GuiConstants;
import it.polimi.ingsw.client.gui.view.component.RoomBox;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.List;

public class LobbyPresentation extends AbstractPreGamePresentation {

    public LobbyPresentation(GuiAssets assets) {
        super(assets);
    }

    public Pane generatePresentation(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height,
                                     List<RoomBox> rooms,
                                     Button action
    ) {
        style(action);

        StackPane center = new StackPane();
        StackPane bottom = new StackPane(action);

        if (rooms.size() <= 0) {
            Text noRoomsText = new Text("No rooms found, please create a new one\n\nYou can also wait for other players to create a room");
            style(noRoomsText);
            center.getChildren().add(noRoomsText);
            return super.generatePresentation(width, height, center, bottom);
        }

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

            style(roomBox.getName());
            style(roomBox.getPlayers());
            style(roomBox.getSimple());
            style(roomBox.getButton());

            roomsContainer.getChildren().add(roomBox);
        }

        // Add an empty region to distance the scrollbar on the right
        HBox scrollpaneContainer = new HBox();
        scrollpaneContainer.getChildren().add(scrollPane);
        scrollpaneContainer.setSpacing(GuiConstants.DEFAULT_SPACING);
        scrollpaneContainer.getChildren().add(new Text(""));
        center.getChildren().add(scrollpaneContainer);
        return super.generatePresentation(width, height, center, bottom);
    }

}
