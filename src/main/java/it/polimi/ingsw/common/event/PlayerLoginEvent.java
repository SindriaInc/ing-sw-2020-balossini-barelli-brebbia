package it.polimi.ingsw.common.event;

/**
 * Event sent by a player that has just connected
 * This event is the only event accepted when the player is not identified
 * It's used to pair the player to it's server connection
 *
 * View -> Model
 */
public class PlayerLoginEvent extends AbstractPlayerEvent {

    private final int age;

    public PlayerLoginEvent(String player, int age) {
        super(player);
        this.age = age;
    }

    public int getAge() {
        return age;
    }

}
