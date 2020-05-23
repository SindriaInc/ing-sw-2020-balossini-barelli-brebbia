package it.polimi.ingsw.client.gui.view;

import it.polimi.ingsw.client.clientstates.InputState;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class GuiInputView extends AbstractGuiView {

    private InputState state;

    public GuiInputView(InputState state) {
        this.state = state;
    }

    public Scene generateView() {
        Label ipLabel = new Label("Server ip");
        TextField ip = new TextField();
        ip.setPromptText("127.0.0.1");

        Label portLabel = new Label("Server port");
        TextField port = new TextField();
        port.setPromptText("25565");

        Button connect = new Button();
        connect.setText("Connect");
        connect.setOnAction(event -> {
            onConnect(ip.getText(), port.getText());
        });


        Text result = new Text();
        if (state.getData().getLastMessage().isPresent()) {
            result.setText(state.getData().getLastMessage().get());
        }

        // Presentation

        GridPane pane = new GridPane();
        pane.setVgap(15);
        pane.setHgap(15);
        pane.setAlignment(Pos.CENTER);

        pane.add(ipLabel, 0, 1);
        pane.add(ip, 1, 1);

        pane.add(portLabel, 0, 2);
        pane.add(port, 1, 2);

        pane.add(connect, 1, 3);
        pane.add(result, 1, 4);
        return new Scene(pane, 500, 300);
    }

    private void onConnect(String ip, String port) {
        state.acceptIp(ip);
        state.acceptPort(Integer.parseInt(port));
        state.acceptConnect();
    }

}
