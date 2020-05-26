package it.polimi.ingsw.client.gui.view;

import it.polimi.ingsw.client.clientstates.AbstractClientState;
import it.polimi.ingsw.client.clientstates.LoginState;
import it.polimi.ingsw.client.data.LoginData;
import it.polimi.ingsw.client.gui.GuiAssets;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Parent;

public class GuiLoginView extends AbstractGuiFieldsView {

    private final LoginState state;

    public GuiLoginView(LoginState state, GuiAssets images) {
        super(images,
                "Your name", "ExampleName123",
                "Your age", "18",
                "Login"
        );

        this.state = state;
    }

    public Parent generateView(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height) {
        LoginData data = state.getData();
        return super.generateView(data.getLastMessage().orElse(null), width, height);
    }

    @Override
    public AbstractClientState getState() {
        return state;
    }

    @Override
    public void onAction(String name, int age) {
        state.acceptName(name);
        state.acceptAge(age);
        state.acceptLogin();
    }

}
