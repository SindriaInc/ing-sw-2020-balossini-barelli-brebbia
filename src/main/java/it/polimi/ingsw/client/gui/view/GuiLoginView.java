package it.polimi.ingsw.client.gui.view;

import it.polimi.ingsw.client.clientstates.AbstractClientState;
import it.polimi.ingsw.client.clientstates.LoginState;
import it.polimi.ingsw.client.gui.GuiConstants;
import it.polimi.ingsw.client.gui.view.component.IntegerField;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class GuiLoginView extends AbstractGuiView {

    private final LoginState state;

    public GuiLoginView(LoginState state) {
        this.state = state;
    }

    public Parent generateView() {
        Label nameLabel = new Label("Your name");
        TextField name = new TextField();
        name.setPromptText("CoolDude22");

        Label ageLabel = new Label("Your age");
        IntegerField age = new IntegerField();
        age.setPromptText("18");

        Button connect = new Button();
        connect.setText("Login");
        connect.setOnAction(event -> onLogin(name.getText(), age.getValue().orElse(0)));
        connect.disableProperty().bind(age.validBinding().not());

        Text result = new Text();
        if (state.getData().getLastMessage().isPresent()) {
            result.setText(state.getData().getLastMessage().get());
        }

        // Presentation

        GridPane pane = new GridPane();
        pane.setVgap(GuiConstants.DEFAULT_SPACING);
        pane.setHgap(GuiConstants.DEFAULT_SPACING);
        pane.setAlignment(Pos.CENTER);

        pane.add(nameLabel, 0, 1);
        pane.add(name, 1, 1);

        pane.add(ageLabel, 0, 2);
        pane.add(age, 1, 2);

        pane.add(connect, 1, 3);
        pane.add(result, 1, 4);
        return pane;
    }

    @Override
    public AbstractClientState getState() {
        return state;
    }

    private void onLogin(String name, int age) {
        state.acceptName(name);
        state.acceptAge(age);
        state.acceptLogin();
    }

}
