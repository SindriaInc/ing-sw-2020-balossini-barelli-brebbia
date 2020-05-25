package it.polimi.ingsw.client.gui.view.component;

import it.polimi.ingsw.client.gui.GuiConstants;
import it.polimi.ingsw.common.info.RoomInfo;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class RoomBox extends HBox {

    public RoomBox(RoomInfo roomInfo, EventHandler<ActionEvent> handler) {
        Label name = new Label();
        Label players = new Label();
        Label simple = new Label();
        Button button = new Button();

        name.setText(roomInfo.getOwner());
        players.setText(roomInfo.getPlayersCount() + "/" + roomInfo.getMaxPlayers());
        simple.setText(roomInfo.isSimpleGame() ? "Simple" : "Normal");
        button.setText("Join");

        // Presentation

        HBox labelPane = new HBox();
        labelPane.getChildren().add(name);
        labelPane.getChildren().add(players);
        labelPane.getChildren().add(simple);
        labelPane.setSpacing(GuiConstants.DEFAULT_SPACING);
        labelPane.setAlignment(Pos.CENTER_LEFT);

        setHgrow(labelPane, Priority.ALWAYS);
        setSpacing(GuiConstants.DEFAULT_SPACING);
        setAlignment(Pos.CENTER);

        getChildren().add(labelPane);
        getChildren().add(button);

        button.setOnAction(handler);
    }

}
