package it.polimi.ingsw.common.event.lobby;

import it.polimi.ingsw.common.event.AbstractPlayerEvent;
import it.polimi.ingsw.controller.ResponseEventProvider;
import it.polimi.ingsw.view.ViewEventProvider;

/**
 * Lobby events are events sent only by the model
 * They may be sent multiple times to update the state of the lobby or room, the client should update every time
 */
public abstract class AbstractLobbyEvent extends AbstractPlayerEvent {

    /**
     * Abstract class constructor
     *
     * @param player The player that receives the event
     */
    public AbstractLobbyEvent(String player) {
        super(player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void accept(ResponseEventProvider provider) {
        super.accept(provider);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void accept(ViewEventProvider provider) {
        super.accept(provider);
    }

}
