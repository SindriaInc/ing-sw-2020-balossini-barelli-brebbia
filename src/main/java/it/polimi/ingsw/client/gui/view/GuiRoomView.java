package it.polimi.ingsw.client.gui.view;

import it.polimi.ingsw.client.clientstates.AbstractClientState;
import it.polimi.ingsw.client.clientstates.RoomState;
import it.polimi.ingsw.client.gui.GuiAssets;
import it.polimi.ingsw.client.gui.view.presentation.RoomPresentation;
import it.polimi.ingsw.common.info.RoomInfo;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Parent;
import javafx.scene.text.Text;

public class GuiRoomView extends AbstractGuiView {

    private final RoomState state;

    public GuiRoomView(RoomState state, GuiAssets images) {
        super(images);

        this.state = state;
    }

    public Parent generateView(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height) {
        RoomInfo roomInfo = state.getData().getRoom();

        Text room = new Text();
        room.setText("You are in the room: " + roomInfo.getOwner());

        Text count = new Text();
        count.setText("Players: " + roomInfo.getPlayersCount() + "/" + roomInfo.getMaxPlayers());

        Text start = new Text();
        start.setText("The game will start as soon as the room is full");

        RoomPresentation presentation = new RoomPresentation(getAssets());
        return presentation.generatePresentation(width, height, room, count, start);
    }

    @Override
    public AbstractClientState getState() {
        return state;
    }

}
