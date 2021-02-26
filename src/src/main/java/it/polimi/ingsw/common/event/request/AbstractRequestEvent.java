package it.polimi.ingsw.common.event.request;

import it.polimi.ingsw.common.event.AbstractPlayerEvent;
import it.polimi.ingsw.controller.ResponseEventProvider;
import it.polimi.ingsw.view.ViewEventProvider;

/**
 * Requests events are events sent only by the model
 * The view is expected to reply with an appropriate view event, passing parameters that were present in the request if needed
 */
public abstract class AbstractRequestEvent extends AbstractPlayerEvent {

    /**
     * Abstract class constructor
     *
     * @param player The player that the request is targeted to
     */
    public AbstractRequestEvent(String player) {
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
