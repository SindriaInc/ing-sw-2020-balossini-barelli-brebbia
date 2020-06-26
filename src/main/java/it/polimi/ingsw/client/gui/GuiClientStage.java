package it.polimi.ingsw.client.gui;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GuiClientStage {

    private final Stage stage;

    private final GuiAssets assets;

    private Scene scene;

    public GuiClientStage(Stage stage, GuiAssets assets) {
        this.stage = stage;
        this.assets = assets;
    }

    public void init(Runnable onClose) {
        stage.setTitle("Santorini");

        stage.setOnCloseRequest(event -> {
            onClose.run();
        });

        stage.setMinWidth(GuiConstants.MIN_WIDTH);
        stage.setMinHeight(GuiConstants.MIN_HEIGHT);
        stage.getIcons().add(assets.getImage(GuiAssets.Images.ICON));
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

        // Add the shared stylesheet
        scene.getStylesheets().add(assets.getStyle(GuiAssets.Styles.DEFAULT));
    }

    public void setRoot(Parent root) {
        scene.setRoot(root);
    }

    public ReadOnlyDoubleProperty widthProperty() {
        return scene.widthProperty();
    }

    public ReadOnlyDoubleProperty heightProperty() {
        return scene.heightProperty();
    }

}
