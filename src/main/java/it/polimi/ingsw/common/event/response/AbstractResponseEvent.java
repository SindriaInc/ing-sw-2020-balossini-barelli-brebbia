package it.polimi.ingsw.common.event.response;

import it.polimi.ingsw.common.event.AbstractPlayerEvent;
import it.polimi.ingsw.model.ModelEventProvider;
import it.polimi.ingsw.view.ViewEventProvider;

/**
 * Response events are events sent only by the controller
 * They are sent to signal that the previous event sent by the view was not accepted by the model
 */
public abstract class AbstractResponseEvent extends AbstractPlayerEvent {

    /**
     * Abstract class constructor
     *
     * @param player The player that will receive the event
     */
    public AbstractResponseEvent(String player) {
        super(player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void accept(ModelEventProvider provider) {
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
