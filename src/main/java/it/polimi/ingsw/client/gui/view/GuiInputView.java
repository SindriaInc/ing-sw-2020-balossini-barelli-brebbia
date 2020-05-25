package it.polimi.ingsw.client.gui.view;

import it.polimi.ingsw.client.clientstates.AbstractClientState;
import it.polimi.ingsw.client.clientstates.InputState;
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

public class GuiInputView extends AbstractGuiView {

    private final InputState state;

    public GuiInputView(InputState state) {
        this.state = state;
    }

    public Parent generateView() {
        Label ipLabel = new Label("Server ip");
        TextField ip = new TextField();
        ip.setPromptText("127.0.0.1");

        Label portLabel = new Label("Server port");
        IntegerField port = new IntegerField();
        port.setPromptText("25565");

        Button connect = new Button();
        connect.setText("Connect");
        connect.disableProperty().bind(port.validBinding().not());
        connect.setOnAction(event -> onConnect(ip.getText(), port.getValue().orElse(0)));

        Text result = new Text();
        if (state.getData().getLastMessage().isPresent()) {
            result.setText(state.getData().getLastMessage().get());
        }

        // Presentation

        GridPane pane = new GridPane();
        pane.setVgap(GuiConstants.DEFAULT_SPACING);
        pane.setHgap(GuiConstants.DEFAULT_SPACING);
        pane.setAlignment(Pos.CENTER);

        pane.add(ipLabel, 0, 1);
        pane.add(ip, 1, 1);

        pane.add(portLabel, 0, 2);
        pane.add(port, 1, 2);

        pane.add(connect, 1, 3);
        pane.add(result, 1, 4);
        return pane;
    }

    @Override
    public AbstractClientState getState() {
        return state;
    }

    private void onConnect(String ip, int port) {
        state.acceptIp(ip);
        state.acceptPort(port);
        state.acceptConnect();
    }

}
