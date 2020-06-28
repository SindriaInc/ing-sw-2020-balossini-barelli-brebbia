package it.polimi.ingsw.controller;

import it.polimi.ingsw.common.IResponseEventProvider;
import it.polimi.ingsw.common.Observable;
import it.polimi.ingsw.common.Observer;
import it.polimi.ingsw.common.event.response.ResponseInvalidLoginEvent;
import it.polimi.ingsw.common.event.response.ResponseInvalidParametersEvent;
import it.polimi.ingsw.common.event.response.ResponseInvalidPlayerEvent;
import it.polimi.ingsw.common.event.response.ResponseInvalidStateEvent;

public class ResponseEventProvider implements IResponseEventProvider {

    /**
     * Observable for ResponseInvalidLoginEvent
     */
    private final Observable<ResponseInvalidLoginEvent> responseInvalidLoginEventObservable = new Observable<>();

    /**
     * Observable for ResponseInvalidParametersEvent
     */
    private final Observable<ResponseInvalidParametersEvent> responseInvalidParametersEventObservable = new Observable<>();

    /**
     * Observable for ResponseInvalidPlayerEvent
     */
    private final Observable<ResponseInvalidPlayerEvent> responseInvalidPlayerEventObservable = new Observable<>();

    /**
     * Observable for ResponseInvalidStateEvent
     */
    private final Observable<ResponseInvalidStateEvent> responseInvalidStateEventObservable = new Observable<>();

    /**
     * @see IResponseEventProvider#registerResponseInvalidLoginEventObserver(Observer)
     */
    @Override
    public void registerResponseInvalidLoginEventObserver(Observer<ResponseInvalidLoginEvent> observer) {
        responseInvalidLoginEventObservable.register(observer);
    }

    /**
     * @see IResponseEventProvider#registerResponseInvalidParametersEventObserver(Observer)
     */
    @Override
    public void registerResponseInvalidParametersEventObserver(Observer<ResponseInvalidParametersEvent> observer) {
        responseInvalidParametersEventObservable.register(observer);
    }

    /**
     * @see IResponseEventProvider#registerResponseInvalidPlayerEventObserver(Observer)
     */
    @Override
    public void registerResponseInvalidPlayerEventObserver(Observer<ResponseInvalidPlayerEvent> observer) {
        responseInvalidPlayerEventObservable.register(observer);
    }

    /**
     * @see IResponseEventProvider#registerResponseInvalidStateEventObserver(Observer)
     */
    @Override
    public void registerResponseInvalidStateEventObserver(Observer<ResponseInvalidStateEvent> observer) {
        responseInvalidStateEventObservable.register(observer);
    }

    public Observable<ResponseInvalidLoginEvent> getResponseInvalidLoginEventObservable() {
        return responseInvalidLoginEventObservable;
    }

    public Observable<ResponseInvalidParametersEvent> getResponseInvalidParametersEventObservable() {
        return responseInvalidParametersEventObservable;
    }

    public Observable<ResponseInvalidPlayerEvent> getResponseInvalidPlayerEventObservable() {
        return responseInvalidPlayerEventObservable;
    }

    public Observable<ResponseInvalidStateEvent> getResponseInvalidStateEventObservable() {
        return responseInvalidStateEventObservable;
    }

}
