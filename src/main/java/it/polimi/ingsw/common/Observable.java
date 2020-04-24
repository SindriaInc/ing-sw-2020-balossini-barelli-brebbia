package it.polimi.ingsw.common;

import java.util.ArrayList;
import java.util.List;

public class Observable<T> {

    private final List<Observer<T>> observers = new ArrayList<>();

    public void register(Observer<T> observer) {
        observers.add(observer);
    }

    public void notifyObservers(T object) {
        for (Observer<T> observer : observers) {
            observer.onEvent(object);
        }
    }

}
