package it.polimi.ingsw.client.gui.view;

import it.polimi.ingsw.client.clientstates.AbstractClientState;
import it.polimi.ingsw.client.clientstates.GameState;
import it.polimi.ingsw.client.data.GameData;
import it.polimi.ingsw.client.gui.view.game.GuiBoardView;
import it.polimi.ingsw.client.gui.view.game.GuiGodsView;
import javafx.scene.Parent;
import javafx.scene.control.Alert;

public class GuiGameView extends AbstractGuiView {

    private final GameState state;

    private final GuiGodsView godsView;
    private final GuiBoardView boardView;

    public GuiGameView(GameState state) {
        this.state = state;

        godsView = new GuiGodsView(state);
        boardView = new GuiBoardView(state);
    }

    @Override
    public Parent generateView() {
        GameData data = state.getData();

        if (data.getLastMessage().isPresent()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText(data.getLastMessage().get());
            alert.showAndWait();
        }

        if (data.isInGodsPhase()) {
            godsView.setState(state);
            return godsView.generateView();
        }

        boardView.setState(state);
        boardView.generateView();
        return boardView.generateView();
    }

    @Override
    public AbstractClientState getState() {
        return state;
    }

}
