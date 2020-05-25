package it.polimi.ingsw.client.gui.view.component;

import it.polimi.ingsw.client.gui.GuiConstants;
import it.polimi.ingsw.common.info.GodInfo;
import it.polimi.ingsw.common.info.RoomInfo;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class GodBox extends HBox {

    public GodBox(GodInfo godInfo, StringBinding buttonText, BooleanBinding buttonEnable, EventHandler<ActionEvent> handler) {
        Label name = new Label();
        Label title = new Label();
        Label id = new Label();
        Label description = new Label();
        Label type = new Label();
        Button button = new Button();

        name.setText(godInfo.getName());
        title.setText(godInfo.getTitle());
        id.setText("ID: #" + godInfo.getId());
        type.setText(godInfo.getType());
        description.setText(godInfo.getDescription());
        button.textProperty().bind(buttonText);
        button.disableProperty().bind(buttonEnable.not());

        // Presentation

        name.setMinWidth(GuiConstants.GOD_NAME_SIZE);
        title.setMinWidth(GuiConstants.GOD_TITLE_SIZE);
        description.setMaxWidth(GuiConstants.GOD_DESCRIPTION_SIZE);
        description.setWrapText(true);

        GridPane labelPane = new GridPane();
        labelPane.add(name, 0, 0);
        labelPane.add(title, 1, 0);
        labelPane.add(id, 0, 1);
        labelPane.add(type, 1, 1);
        labelPane.add(description, 2, 0);
        GridPane.setColumnSpan(description, GridPane.REMAINING);
        GridPane.setRowSpan(description, GridPane.REMAINING);
        GridPane.setFillWidth(description, true);
        labelPane.setHgap(GuiConstants.DEFAULT_SPACING);
        labelPane.setVgap(GuiConstants.DEFAULT_SPACING);
        labelPane.setAlignment(Pos.CENTER_LEFT);

        setHgrow(labelPane, Priority.ALWAYS);
        setSpacing(GuiConstants.DEFAULT_SPACING);
        setAlignment(Pos.CENTER);

        getChildren().add(labelPane);
        getChildren().add(button);

        button.setOnAction(handler);
    }

}
