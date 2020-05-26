package it.polimi.ingsw.client.gui.view;

import it.polimi.ingsw.client.clientstates.AbstractClientState;
import it.polimi.ingsw.client.clientstates.LobbyState;
import it.polimi.ingsw.client.gui.GuiConstants;
import it.polimi.ingsw.client.gui.GuiAssets;
import it.polimi.ingsw.client.gui.view.component.RoomBox;
import it.polimi.ingsw.client.gui.view.dialog.CreateRoomDialog;
import it.polimi.ingsw.common.info.RoomInfo;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GuiLobbyView extends AbstractGuiView {

    private final LobbyState state;

    public GuiLobbyView(LobbyState state, GuiAssets assets) {
        super(assets);

        this.state = state;
    }

    public Parent generateView(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height) {
        List<RoomInfo> rooms = state.getData().getRooms();

        ListView<RoomBox> roomsView = new ListView<>();
        ObservableList<RoomBox> roomComponents = FXCollections.observableList(
                rooms.stream().map(room -> new RoomBox(room, (ignored) -> onJoin(room.getOwner()))).collect(Collectors.toList())
        );
        roomsView.setItems(roomComponents);

        Button create = new Button();
        create.setText("Create a new room");
        create.setOnAction(event -> {
            CreateRoomDialog dialog = new CreateRoomDialog();
            Optional<Pair<Integer, Boolean>> result = dialog.showAndWait();

            if (result.isEmpty()) {
                return;
            }

            onCreate(result.get().getKey(), result.get().getValue());
        });

        Text result = new Text();
        if (state.getData().getLastMessage().isPresent()) {
            result.setText(state.getData().getLastMessage().get());
        }

        Node actualRoomsView = roomsView;

        if (roomComponents.size() == 0) {
            actualRoomsView = new Text("No rooms found, please create a new one\n\nYou can also wait for other players to create a room");
        }

        // Presentation

        BorderPane pane = new BorderPane();
        BorderPane.setMargin(create, new Insets(GuiConstants.DEFAULT_SPACING));
        BorderPane.setAlignment(create, Pos.CENTER_RIGHT);
        pane.setCenter(actualRoomsView);
        pane.setBottom(create);

        return pane;
    }

    @Override
    public AbstractClientState getState() {
        return state;
    }

    private void onJoin(String owner) {
        state.acceptJoin(owner);
    }

    private void onCreate(int maxPlayers, boolean simpleGame) {
        state.acceptCreate(simpleGame, maxPlayers);
    }

}
