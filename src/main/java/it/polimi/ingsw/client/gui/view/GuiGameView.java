package it.polimi.ingsw.client.gui.view;

import it.polimi.ingsw.client.clientstates.AbstractClientState;
import it.polimi.ingsw.client.clientstates.GameState;
import it.polimi.ingsw.client.data.GameData;
import it.polimi.ingsw.client.gui.GuiAssets;
import it.polimi.ingsw.client.gui.view.game.GuiBoardView;
import it.polimi.ingsw.client.gui.view.game.GuiGodsView;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Parent;
import javafx.scene.control.Alert;

/**
 * A gui view of the game state
 */
public class GuiGameView extends AbstractGuiView {

    /**
     * The game data
     */
    private final GameState state;

    /**
     * The gods view
     */
    private final GuiGodsView godsView;

    /**
     * The board view
     */
    private final GuiBoardView boardView;

    /**
     * Class constructor, set game state and assets
     * @param state The game state
     * @param assets The assets
     */
    public GuiGameView(GameState state, GuiAssets assets) {
        super(assets);

        this.state = state;

        godsView = new GuiGodsView(state, assets);
        boardView = new GuiBoardView(state, assets);
    }

    /**
     * @see AbstractGuiView#generateView(ReadOnlyDoubleProperty, ReadOnlyDoubleProperty)
     */
    @Override
    public Parent generateView(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height) {
        GameData data = state.getData();

        if (data.getLastMessage().isPresent()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText(data.getLastMessage().get());
            alert.showAndWait();
        }

        if (data.isInGodsPhase()) {
            godsView.setState(state);
            return godsView.generateView(width, height);
        }

        boardView.setState(state);
        return boardView.generateView(width, height);
    }

    /**
     * @see AbstractGuiView#getState()
     */
    @Override
    public AbstractClientState getState() {
        return state;
    }

}
