package it.polimi.ingsw.client.gui.view;

import it.polimi.ingsw.client.clientstates.AbstractClientState;
import it.polimi.ingsw.client.clientstates.InputState;
import it.polimi.ingsw.client.data.InputData;
import it.polimi.ingsw.client.gui.GuiAssets;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Parent;
import javafx.scene.control.Alert;

/**
 * A gui view of the input state
 */
public class GuiInputView extends AbstractGuiFieldsView {

    /**
     * The game state
     */
    private final InputState state;

    /**
     * The current alert
     */
    private Alert currentAlert;

    /**
     * Class constructor, set game state and assets
     * @param state The game state
     * @param images The assets
     */
    public GuiInputView(InputState state, GuiAssets images) {
        super(images,
                "Server ip", "127.0.0.1",
                "Server port", "25565",
                "Connect"
        );

        this.state = state;
    }

    /**
     * @see AbstractGuiFieldsView#generateView(ReadOnlyDoubleProperty, ReadOnlyDoubleProperty)
     */
    public Parent generateView(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height) {
        InputData data = state.getData();
        return super.generateView(data.getLastMessage().orElse(null), width, height);
    }

    /**
     * @see AbstractGuiFieldsView#getState()
     */
    @Override
    public AbstractClientState getState() {
        return state;
    }

    /**
     * @see AbstractGuiFieldsView#onAction(String, int)
     */
    @Override
    public void onAction(String ip, int port) {
        state.acceptIp(ip);
        state.acceptPort(port);
        state.acceptConnect();
    }

}
