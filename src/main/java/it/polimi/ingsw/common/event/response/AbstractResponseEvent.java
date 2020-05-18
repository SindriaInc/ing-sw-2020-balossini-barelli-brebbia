package it.polimi.ingsw.common.event.response;

import it.polimi.ingsw.common.event.AbstractPlayerEvent;
import it.polimi.ingsw.model.ModelEventProvider;
import it.polimi.ingsw.view.ViewEventProvider;

import java.util.Optional;

/**
 * Response events are events sent only by the controller
 * They are sent to signal that the previous event sent by the view was not accepted by the model
 */
public abstract class AbstractResponseEvent extends AbstractPlayerEvent {

    public AbstractResponseEvent(String player) {
        super(player);
    }

    @Override
    public Optional<String> getSender() {
        return Optional.empty();
    }

    public final void accept(ModelEventProvider provider) {
        super.accept(provider);
    }

    public final void accept(ViewEventProvider provider) {
        super.accept(provider);
    }

}
