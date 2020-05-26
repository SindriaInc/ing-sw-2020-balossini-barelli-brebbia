package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.AbstractClientViewer;
import it.polimi.ingsw.client.ClientConnector;
import it.polimi.ingsw.client.clientstates.*;
import it.polimi.ingsw.client.gui.view.*;
import it.polimi.ingsw.common.logging.Logger;
import it.polimi.ingsw.common.logging.reader.ConsoleLogReader;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;

public class GuiClientViewer extends AbstractClientViewer {

    private GuiClientStage stage;

    private GuiAssets assets;

    private AbstractGuiView currentView;

    public GuiClientViewer(ExecutorService executorService) {
        Logger.getInstance().addReader(new ConsoleLogReader(System.out));

        Platform.startup(() -> {
            assets = new GuiAssets();

            stage = new GuiClientStage(new Stage());
            stage.init();

            // Initialize the client after the application has loaded
            new ClientConnector(this);
        });
    }

    @Override
    public void viewInput(InputState state) {
        updateScene(new GuiInputView(state, assets));
    }

    @Override
    public void viewLogin(LoginState state) {
        updateScene(new GuiLoginView(state, assets));
    }

    @Override
    public void viewLobby(LobbyState state) {
        updateScene(new GuiLobbyView(state, assets));
    }

    @Override
    public void viewRoom(RoomState state) {
        updateScene(new GuiRoomView(state, assets));
    }

    @Override
    public void viewGame(GameState state) {
        updateScene(new GuiGameView(state, assets));
    }

    @Override
    public void viewEnd(EndState state) {
        updateScene(new GuiEndView(state, assets));
    }

    private void updateScene(AbstractGuiView view) {
        // Only update the view instance if a different state is passed
        if (currentView == null || !currentView.getState().equals(view.getState())) {
            currentView = view;
        }

        Platform.runLater(() -> {
            if (!stage.hasScene()) {
                StackPane splashScreen = new StackPane();
                stage.setScene(new Scene(splashScreen, GuiConstants.DEFAULT_WIDTH, GuiConstants.DEFAULT_HEIGHT));
            }

            Parent root = currentView.generateView(stage.widthProperty(), stage.heightProperty());
            stage.setRoot(root);
            root.layout();
        });
    }

}
