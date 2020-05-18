package it.polimi.ingsw.common.event;

import it.polimi.ingsw.model.ModelEventProvider;
import it.polimi.ingsw.view.ViewEventProvider;

import java.util.List;

/**
 * Event for the god selection by the challenger
 *
 * View -> Model
 * Model -> View
 */
public class PlayerChallengerSelectGodsEvent extends AbstractPlayerEvent {

    private final List<String> gods;

    public PlayerChallengerSelectGodsEvent(String player, List<String> gods) {
        super(player);

        this.gods = gods;
    }

    public List<String> getGods() {
        return gods;
    }

    @Override
    public void accept(ModelEventProvider provider) {
        provider.getPlayerChallengerSelectGodsEventObservable().notifyObservers(this);
    }

    @Override
    public void accept(ViewEventProvider provider) {
        provider.getPlayerChallengerSelectGodsEventObservable().notifyObservers(this);
    }

}
