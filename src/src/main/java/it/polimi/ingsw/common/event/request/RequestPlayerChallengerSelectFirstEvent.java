package it.polimi.ingsw.common.event.request;

import it.polimi.ingsw.model.ModelEventProvider;

import java.util.List;

/**
 * Request a PlayerChallengerSelectFirstEvent response from the player
 */
public class RequestPlayerChallengerSelectFirstEvent extends AbstractRequestEvent {

    /**
     * The list of the players that the challenger can choose
     */
    private final List<String> players;

    /**
     * Class constructor
     *
     * @param player The player that the request is targeted to
     * @param players The list of players
     */
    public RequestPlayerChallengerSelectFirstEvent(String player, List<String> players) {
        super(player);

        this.players = players;
    }

    /**
     * A copy of the list of the list of players
     *
     * @return The list
     */
    public List<String> getPlayers() {
        return List.copyOf(players);
    }

    @Override
    public void accept(ModelEventProvider provider) {
        provider.getRequestPlayerChallengerSelectFirstEventObservable().notifyObservers(this);
    }

}
