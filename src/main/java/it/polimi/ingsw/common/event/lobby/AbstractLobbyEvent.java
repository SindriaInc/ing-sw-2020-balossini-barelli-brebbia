package it.polimi.ingsw.common.event.lobby;

import it.polimi.ingsw.common.event.AbstractPlayerEvent;
import it.polimi.ingsw.controller.ResponseEventProvider;
import it.polimi.ingsw.view.ViewEventProvider;

/**
 * Lobby events are events sent only by the model
 * They may be sent multiple times to update the state of the lobby or room, the client should update every time
 */
public abstract class AbstractLobbyEvent extends AbstractPlayerEvent {

    public AbstractLobbyEvent(String player) {
        super(player);
    }

    public final void accept(ResponseEventProvider provider) {
        // Lobby events are sent only by the model
        super.accept(provider);
    }

    public final void accept(ViewEventProvider provider) {
        // Lobby events are sent only by the model
        super.accept(provider);
    }

}
