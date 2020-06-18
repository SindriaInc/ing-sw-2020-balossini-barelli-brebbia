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

    private final String first;

    public PlayerChallengerSelectFirstEvent(String player, String first) {
        super(player);

        this.first = first;
    }

    public String getFirst() {
        return first;
    }

    @Override
    public void accept(ModelEventProvider provider) {
        provider.getPlayerChallengerSelectFirstEventObservable().notifyObservers(this);
    }

    @Override
    public void accept(ViewEventProvider provider) {
        provider.getPlayerChallengerSelectFirstEventObservable().notifyObservers(this);
    }

}
