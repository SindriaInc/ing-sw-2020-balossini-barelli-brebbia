package it.polimi.ingsw.common.events;

import java.util.List;

/**
 * Event for the god selection by the challenger
 *
 * View -> Model
 * Model -> View
 */
public class ChallengerSelectGodsEvent {

    private final List<String> gods;

    public ChallengerSelectGodsEvent(List<String> gods) {
        this.gods = gods;
    }

    public List<String> getGods() {
        return gods;
    }

}
