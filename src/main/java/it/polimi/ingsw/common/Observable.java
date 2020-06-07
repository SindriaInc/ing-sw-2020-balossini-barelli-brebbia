package it.polimi.ingsw.common;

import java.util.ArrayList;
import java.util.List;

public class Observable<T> {

    /**
     * The list of observers, kept in order of insertion
     */
    private final List<Observer<T>> observers = new ArrayList<>();

    /**
     * Registers an observer
     */
    public void register(Observer<T> observer) {
        observers.add(observer);
    }

    /**
     * Notifies all observers in the observers list
     */
    public void notifyObservers(T object) {
        for (Observer<T> observer : observers) {
            observer.onEvent(object);
        }
    }

}
