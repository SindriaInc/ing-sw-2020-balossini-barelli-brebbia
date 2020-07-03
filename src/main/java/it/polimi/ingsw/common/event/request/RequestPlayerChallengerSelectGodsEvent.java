package it.polimi.ingsw.common.event.request;

import it.polimi.ingsw.common.info.GodInfo;
import it.polimi.ingsw.model.ModelEventProvider;

import java.util.List;

/**
 * Request a PlayerChallengerSelectGodsEvent response from the player
 */
public class RequestPlayerChallengerSelectGodsEvent extends AbstractRequestEvent {

    /**
     * The list of gods that the challenger can choose
     */
    private final List<GodInfo> gods;

    /**
     * The number of gods that the challenger must select
     */
    private final int selectedGodsCount;

    /**
     * Class constructor
     *
     * @param player The player that the request is targeted to
     * @param gods The list of gods
     * @param selectedGodsCount The gods to be selected
     */
    public RequestPlayerChallengerSelectGodsEvent(String player, List<GodInfo> gods, int selectedGodsCount) {
        super(player);

        this.gods = gods;
        this.selectedGodsCount = selectedGodsCount;
    }

    /**
     * A copy of the list of the available gods
     *
     * @return The list
     */
    public List<GodInfo> getGods() {
        return List.copyOf(gods);
    }

    public int getSelectedGodsCount() {
        return selectedGodsCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ModelEventProvider provider) {
        provider.getRequestPlayerChallengerSelectGodsEventObservable().notifyObservers(this);
    }

}
