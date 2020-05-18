package it.polimi.ingsw.common.event;

import it.polimi.ingsw.view.ViewEventProvider;

/**
 * Event send by a player that wants to create a new room
 *
 * View -> Model
 */
public class PlayerCreateRoomEvent extends AbstractPlayerEvent {

    private final int maxPlayers;

    private final boolean simpleGame;

    public PlayerCreateRoomEvent(String player, int maxPlayers, boolean simpleGame) {
        super(player);
        this.maxPlayers = maxPlayers;
        this.simpleGame = simpleGame;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public boolean isSimpleGame() {
        return simpleGame;
    }

    @Override
    public void accept(ViewEventProvider provider) {
        provider.getPlayerCreateRoomEventObservable().notifyObservers(this);
    }

}
