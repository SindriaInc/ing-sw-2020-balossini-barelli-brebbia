package it.polimi.ingsw.client.gui.view;

import it.polimi.ingsw.client.clientstates.AbstractClientState;
import it.polimi.ingsw.client.clientstates.InputState;
import it.polimi.ingsw.client.data.InputData;
import it.polimi.ingsw.client.gui.GuiAssets;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Parent;
import javafx.scene.control.Alert;

public class GuiInputView extends AbstractGuiFieldsView {

    private final InputState state;

    private Alert currentAlert;

    public GuiInputView(InputState state, GuiAssets images) {
        super(images,
                "Server ip", "127.0.0.1",
                "Server port", "25565",
                "Connect"
        );

        this.state = state;
    }

    public Parent generateView(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height) {
        InputData data = state.getData();
        return super.generateView(data.getLastMessage().orElse(null), width, height);
    }

    @Override
    public AbstractClientState getState() {
        return state;
    }

    @Override
    public void onAction(String ip, int port) {
        state.acceptIp(ip);
        state.acceptPort(port);
        state.acceptConnect();
    }

}
