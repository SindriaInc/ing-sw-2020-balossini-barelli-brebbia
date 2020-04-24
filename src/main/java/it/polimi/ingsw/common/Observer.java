package it.polimi.ingsw.common;

public interface Observer<T> {

    void onEvent(T event);

}
