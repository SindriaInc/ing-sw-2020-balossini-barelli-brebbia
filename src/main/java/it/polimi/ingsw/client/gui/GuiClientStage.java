package it.polimi.ingsw.client.gui;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GuiClientStage {

    private final Stage stage;

    private Scene scene;

    public GuiClientStage(Stage stage) {
        this.stage = stage;
    }

    public void init() {
        stage.setTitle("Santorini");

        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        stage.setMinWidth(GuiConstants.MIN_WIDTH);
        stage.setMinHeight(GuiConstants.MIN_HEIGHT);
    }

    public boolean hasScene() {
        return scene != null;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        stage.setScene(scene);

        if (!stage.isShowing()) {
            stage.show();
        }
    }

    public void setRoot(Parent root) {
        scene.setRoot(root);
    }

}