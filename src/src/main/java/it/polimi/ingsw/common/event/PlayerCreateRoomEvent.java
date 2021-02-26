package it.polimi.ingsw.common.event;

import it.polimi.ingsw.view.ViewEventProvider;

/**
 * Event send by a player that wants to create a new room
 *
 * View -> Model
 */
public class PlayerCreateRoomEvent extends AbstractPlayerEvent {

    /**
     * The maximum number of players
     */
    private final int maxPlayers;

    /**
     * Whether or not the game will be simple
     */
    private final boolean simpleGame;

    /**
     * Class constructor
     *
     * @param player The player creating the room
     * @param maxPlayers The number of players
     * @param simpleGame Whether the game will be simple
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ViewEventProvider provider) {
        provider.getPlayerCreateRoomEventObservable().notifyObservers(this);
    }

}
