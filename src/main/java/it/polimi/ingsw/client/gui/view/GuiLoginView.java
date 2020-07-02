package it.polimi.ingsw.client.gui.view;

import it.polimi.ingsw.client.clientstates.AbstractClientState;
import it.polimi.ingsw.client.clientstates.LoginState;
import it.polimi.ingsw.client.data.LoginData;
import it.polimi.ingsw.client.gui.GuiAssets;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Parent;

/**
 * A gui view of the login view
 */
public class GuiLoginView extends AbstractGuiFieldsView {

    /**
     * The login data
     */
    private final LoginState state;

    /**
     * Class constructor, set assets and state
     * @param state The state
     * @param images The assets
     */
    public GuiLoginView(LoginState state, GuiAssets images) {
        super(images,
                "Your name", "ExampleName123",
                "Your age", "18",
                "Login"
        );

        this.state = state;
    }

    /**
     * @see AbstractGuiFieldsView#generateView(ReadOnlyDoubleProperty, ReadOnlyDoubleProperty)
     */
    public Parent generateView(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height) {
        LoginData data = state.getData();
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
    public void onAction(String name, int age) {
        state.acceptName(name);
        state.acceptAge(age);
        state.acceptLogin();
    }

}
