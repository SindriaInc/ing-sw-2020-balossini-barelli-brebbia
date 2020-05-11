package it.polimi.ingsw.common.event;

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

}
