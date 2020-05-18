package it.polimi.ingsw.common.event.request;

import it.polimi.ingsw.model.ModelEventProvider;

import java.util.List;

/**
 * Request a PlayerChallengerSelectGodsEvent response from the player
 */
public class RequestPlayerChallengerSelectGodsEvent extends AbstractRequestEvent {

    public static final String ATTRIBUTE_GODS = "gods";
    public static final String ATTRIBUTE_SELECTED_GODS_COUNT = "selectedGodsCount";

    private final List<String> gods;

    private final int selectedGodsCount;

    public RequestPlayerChallengerSelectGodsEvent(String player, List<String> gods, int selectedGodsCount) {
        super(player);

        this.gods = gods;
        this.selectedGodsCount = selectedGodsCount;
    }

    public List<String> getGods() {
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
