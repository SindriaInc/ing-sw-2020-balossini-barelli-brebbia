package it.polimi.ingsw.common.event.request;

import it.polimi.ingsw.model.ModelEventProvider;

import java.util.List;

/**
 * Request a PlayerChallengerSelectFirstEvent response from the player
 */
public class RequestPlayerChallengerSelectFirstEvent extends AbstractRequestEvent {

    private final List<String> players;

    public RequestPlayerChallengerSelectFirstEvent(String player, List<String> players) {
        super(player);

        this.players = players;
    }

    public List<String> getPlayers() {
        return List.copyOf(players);
    }

    @Override
    public void accept(ModelEventProvider provider) {
        provider.getRequestPlayerChallengerSelectFirstEventObservable().notifyObservers(this);
    }

}
