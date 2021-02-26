package it.polimi.ingsw.common.event;

import it.polimi.ingsw.model.ModelEventProvider;
import it.polimi.ingsw.view.ViewEventProvider;

/**
 * Event for the first player selection by the challenger
 *
 * View -> Model
 * Model -> View
 */
public class PlayerChallengerSelectFirstEvent extends AbstractPlayerEvent {

    /**
     * The player that will make the first move
     */
    private final String first;

    /**
     * Class constructor
     *
     * @param player The challenger selecting the first player
     * @param first The first player
     */
    public PlayerChallengerSelectFirstEvent(String player, String first) {
        super(player);

        this.first = first;
    }

    public String getFirst() {
        return first;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ModelEventProvider provider) {
        provider.getPlayerChallengerSelectFirstEventObservable().notifyObservers(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ViewEventProvider provider) {
        provider.getPlayerChallengerSelectFirstEventObservable().notifyObservers(this);
    }

}
