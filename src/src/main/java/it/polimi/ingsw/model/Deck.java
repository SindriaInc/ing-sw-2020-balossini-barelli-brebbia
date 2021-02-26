package it.polimi.ingsw.model;
import java.util.List;

/**
 * The class representing the deck, that contains a list of god that can ve used
 * by the players during a game.
 */
public class Deck {

    /**
     * List of god's cards which can be chosen by the players
     */
    private final List<God> gods;

    /**
     * Class constructor
     * @param gods The list of gods which the deck will contain
     */
    public Deck(List<God> gods) {
        this.gods = gods;
    }

    public List<God> getGods() {
        return gods;
    }

}
