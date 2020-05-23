package it.polimi.ingsw.client.gui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GuiClientStage {

    private final Stage stage;

    public GuiClientStage(Stage stage) {
        this.stage = stage;
    }

    public void init() {
        stage.setTitle("Santorini");

        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        stage.show();
    }

    public void setScene(Scene scene) {
        stage.setScene(scene);
    }

}
