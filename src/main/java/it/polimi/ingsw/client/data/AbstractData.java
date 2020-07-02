package it.polimi.ingsw.client.data;

import java.util.Optional;

/**
 * Each client state should have an associated data class, representing the state's information storage
 * Data can be used by views to display information to the user
 */
public class AbstractData {

    /**
     * The òast message
     */
    private final String lastMessage;

    public AbstractData(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Optional<String> getLastMessage() {
        return Optional.ofNullable(lastMessage);
    }

}
