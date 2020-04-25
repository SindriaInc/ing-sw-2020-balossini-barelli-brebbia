package it.polimi.ingsw.common.events;

import it.polimi.ingsw.model.God;

import java.util.List;

/**
 * Event for the god selection by the challenger
 */
public class ChallengerSelectGodsEvent {

    private final List<God> gods;

    public ChallengerSelectGodsEvent(List<God> gods) {
        this.gods = gods;
    }

    public List<God> getGods() {
        return gods;
    }

}
