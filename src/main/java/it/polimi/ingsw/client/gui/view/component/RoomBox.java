package it.polimi.ingsw.client.gui.view.component;

import it.polimi.ingsw.client.gui.GuiConstants;
import it.polimi.ingsw.common.info.RoomInfo;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class RoomBox extends HBox {

    /**
     * The box containing the room data
     */
    private final HBox labelBox;

    /**
     * The room owner's name
     */
    private final Label name;

    /**
     * The number of players
     */
    private final Label players;

    /**
     * Whether the game is simple or not
     */
    private final Label simple;

    /**
     * The join button
     */
    private final Button button;

    public RoomBox(RoomInfo roomInfo, EventHandler<ActionEvent> handler) {
        name = new Label();
        players = new Label();
        simple = new Label();
        button = new Button();

        name.setText(roomInfo.getOwner() + "'s Room");
        players.setText(roomInfo.getPlayersCount() + " out of " + roomInfo.getMaxPlayers());
        simple.setText(roomInfo.isSimpleGame() ? "Simple game" : "Normal game");
        button.setText("Join");

        // Presentation

        labelBox = new HBox();
        labelBox.getChildren().add(name);
        labelBox.getChildren().add(players);
        labelBox.getChildren().add(simple);
        labelBox.setSpacing(GuiConstants.DEFAULT_SPACING);
        labelBox.setAlignment(Pos.CENTER_LEFT);
        labelBox.setPadding(new Insets(GuiConstants.DEFAULT_SPACING));

        setHgrow(labelBox, Priority.ALWAYS);
        setSpacing(GuiConstants.DEFAULT_SPACING);
        setAlignment(Pos.CENTER);

        getChildren().add(labelBox);
        getChildren().add(button);

        button.setOnAction(handler);
    }

    public HBox getLabelBox() {
        return labelBox;
    }

    public Label getName() {
        return name;
    }

    public Label getPlayers() {
        return players;
    }

    public Label getSimple() {
        return simple;
    }

    public Button getButton() {
        return button;
    }

}
