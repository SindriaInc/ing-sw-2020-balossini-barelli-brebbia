package it.polimi.ingsw.common.event;

import it.polimi.ingsw.view.ViewEventProvider;

/**
 * Event sent by a player that has just connected
 * This event is the only event accepted when the player is not identified
 * It's used to pair the player to it's server connection
 *
 * View -> Model
 */
public class PlayerLoginEvent extends AbstractPlayerEvent {

    /**
     * The player's age
     */
    private final int age;

    /**
     * Class constructor
     *
     * @param player The player name
     * @param age The player age
     */
    public PlayerLoginEvent(String player, int age) {
        super(player);
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ViewEventProvider provider) {
        provider.getPlayerLoginEventObservable().notifyObservers(this);
    }

}
