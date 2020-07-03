package it.polimi.ingsw.common.logging;

/**
 * Handles the printing of a message
 */
public interface ILogReader {

    /**
     * Called when a message must be printed
     *
     * @param message The message
     */
    void read(String message);

}
