package it.polimi.ingsw.common.event.request;

import it.polimi.ingsw.common.info.GodInfo;
import it.polimi.ingsw.model.ModelEventProvider;

import java.util.List;

/**
 * Request a PlayerChallengerSelectGodsEvent response from the player
 */
public class RequestPlayerChallengerSelectGodsEvent extends AbstractRequestEvent {

    private final List<GodInfo> gods;

    private final int selectedGodsCount;

    public RequestPlayerChallengerSelectGodsEvent(String player, List<GodInfo> gods, int selectedGodsCount) {
        super(player);

        this.gods = gods;
        this.selectedGodsCount = selectedGodsCount;
    }

    public List<GodInfo> getGods() {
        return gods;
    }

    public int getSelectedGodsCount() {
        return selectedGodsCount;
    }

    @Override
    public void accept(ModelEventProvider provider) {
        provider.getRequestPlayerChallengerSelectGodsEventObservable().notifyObservers(this);
    }

}
