package it.polimi.ingsw.common.event.request;

import it.polimi.ingsw.common.info.GodInfo;
import it.polimi.ingsw.model.ModelEventProvider;

import java.util.List;

/**
 * Request a RequestPlayerChooseGodEvent response from the player
 */
public class RequestPlayerChooseGodEvent extends AbstractRequestEvent {

    private final List<GodInfo> availableGods;

    public RequestPlayerChooseGodEvent(String player, List<GodInfo> availableGods) {
        super(player);

        this.availableGods = availableGods;
    }

    public List<GodInfo> getAvailableGods() {
        return availableGods;
    }

    @Override
    public void accept(ModelEventProvider provider) {
        provider.getRequestPlayerChooseGodEventObservable().notifyObservers(this);
    }

}
