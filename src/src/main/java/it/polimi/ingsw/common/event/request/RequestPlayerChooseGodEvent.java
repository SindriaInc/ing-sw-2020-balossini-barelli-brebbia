package it.polimi.ingsw.common.event.request;

import it.polimi.ingsw.common.info.GodInfo;
import it.polimi.ingsw.model.ModelEventProvider;

import java.util.List;

/**
 * Request a PlayerChooseGodEvent response from the player
 */
public class RequestPlayerChooseGodEvent extends AbstractRequestEvent {

    /**
     * The list of gods that the player can choose
     */
    private final List<GodInfo> availableGods;

    /**
     * Class constructor
     *
     * @param player The player that the request is targeted to
     * @param availableGods The list of gods
     */
    public RequestPlayerChooseGodEvent(String player, List<GodInfo> availableGods) {
        super(player);

        this.availableGods = availableGods;
    }

    /**
     * A copy of the list of the available gods
     *
     * @return The list
     */
    public List<GodInfo> getAvailableGods() {
        return List.copyOf(availableGods);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ModelEventProvider provider) {
        provider.getRequestPlayerChooseGodEventObservable().notifyObservers(this);
    }

}
