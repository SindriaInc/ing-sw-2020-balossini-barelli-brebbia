package it.polimi.ingsw.client.gui.view;

import it.polimi.ingsw.client.clientstates.AbstractClientState;
import it.polimi.ingsw.client.clientstates.LobbyState;
import it.polimi.ingsw.client.data.LobbyData;
import it.polimi.ingsw.client.gui.GuiAssets;
import it.polimi.ingsw.client.gui.view.component.RoomBox;
import it.polimi.ingsw.client.gui.view.dialog.CreateRoomDialog;
import it.polimi.ingsw.client.gui.view.presentation.LobbyPresentation;
import it.polimi.ingsw.common.info.RoomInfo;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GuiLobbyView extends AbstractGuiView {

    private final LobbyState state;

    private CreateRoomDialog dialog;

    public GuiLobbyView(LobbyState state, GuiAssets assets) {
        super(assets);

        this.state = state;
    }

    public Parent generateView(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height) {
        LobbyData data = state.getData();

        List<RoomInfo> rooms = data.getRooms();

        List<RoomBox> roomsView = rooms.stream().map(room -> new RoomBox(room, (ignored) -> onJoin(room.getOwner()))).collect(Collectors.toList());

        Button create = new Button();
        create.setText("Create a new room");
        create.setOnAction(event -> {
            dialog = new CreateRoomDialog(data.getMinGamePlayers(), data.getMaxGamePlayers());
            Optional<Pair<Integer, Boolean>> result = dialog.showAndWait();
            dialog = null;

            if (result.isEmpty()) {
                return;
            }

            onCreate(result.get().getKey(), result.get().getValue());
        });

        Text result = new Text();
        if (data.getLastMessage().isPresent()) {
            result.setText(data.getLastMessage().get());
        }

        LobbyPresentation presentation = new LobbyPresentation(getAssets());
        return presentation.generatePresentation(width, height, roomsView, create);
    }

    @Override
    public AbstractClientState getState() {
        return state;
    }

    @Override
    public void closeWindows() {
        if (dialog != null) {
            dialog.close();
        }
    }

    private void onJoin(String owner) {
        state.acceptJoin(owner);
    }

    private void onCreate(int maxPlayers, boolean simpleGame) {
        state.acceptCreate(simpleGame, maxPlayers);
    }

}
