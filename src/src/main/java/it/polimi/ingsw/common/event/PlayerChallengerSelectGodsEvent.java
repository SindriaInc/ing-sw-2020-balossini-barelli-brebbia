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

    /**
     * The list of selected gods
     */
    private final List<String> gods;

    /**
     * Class constructor
     *
     * @param player The challenger selecting the gods
     * @param gods The gods
     */
    public PlayerChallengerSelectGodsEvent(String player, List<String> gods) {
        super(player);

        this.gods = gods;
    }

    /**
     * A copy of the list of selected gods
     *
     * @return The list
     */
    public List<String> getGods() {
        return List.copyOf(gods);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ModelEventProvider provider) {
        provider.getPlayerChallengerSelectGodsEventObservable().notifyObservers(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ViewEventProvider provider) {
        provider.getPlayerChallengerSelectGodsEventObservable().notifyObservers(this);
    }

}
