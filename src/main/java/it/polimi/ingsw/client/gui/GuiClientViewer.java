package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.AbstractClientViewer;
import it.polimi.ingsw.client.ClientConnector;
import it.polimi.ingsw.client.clientstates.*;
import it.polimi.ingsw.client.gui.view.GuiInputView;
import it.polimi.ingsw.client.gui.view.GuiLoginView;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;

// TODO: Implement
public class GuiClientViewer extends AbstractClientViewer {

    private GuiClientStage stage;

    public GuiClientViewer(ExecutorService executorService) {
        Platform.startup(() -> {
            stage = new GuiClientStage(new Stage());
            stage.init();

            // Initialize the client after the application has loaded
            new ClientConnector(this);
        });
    }

    @Override
    public void viewInput(InputState state) {
        stage.setScene(new GuiInputView(state).generateView());
    }

    @Override
    public void viewLogin(LoginState state) {
        stage.setScene(new GuiLoginView(state).generateView());
    }

    @Override
    public void viewLobby(LobbyState state) {
        // TODO: Implement lobby state in GUI
    }

    @Override
    public void viewRoom(RoomState state) {
        // TODO: Implement room state in GUI
    }

    @Override
    public void viewGame(GameState state) {
        // TODO: Implement game state in GUI
    }

}
