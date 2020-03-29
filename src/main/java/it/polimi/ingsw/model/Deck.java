package it.polimi.ingsw.model;
import java.util.ArrayList;
import java.util.List;

public class Deck {

    /**
     * List of god's cards which can be chosen by the players
     */
    private final List<God> gods = new ArrayList<>();

    public List<God> getGods() {
        return gods;
    }

}
