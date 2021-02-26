package it.polimi.ingsw.common;

/**
 * An observer for an event of type <code>T</code>
 *
 * @param <T> The event type
 */
public interface Observer<T> {

    /**
     * Called when the event occurs
     *
     * @param event The event
     */
    void onEvent(T event);

}
