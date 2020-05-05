package it.polimi.ingsw.common;

import it.polimi.ingsw.common.event.response.ResponseInvalidParametersEvent;
import it.polimi.ingsw.common.event.response.ResponseInvalidPlayerEvent;
import it.polimi.ingsw.common.event.response.ResponseInvalidStateEvent;

public interface IResponseEventProvider {

    /**
     * Register the observer for ResponseInvalidParametersEvent in the related observable
     * @param observer The Observer
     */
    void registerResponseInvalidParametersEventObserver(Observer<ResponseInvalidParametersEvent> observer);

    /**
     * Register the observer for ResponseInvalidPlayerEvent in the related observable
     * @param observer The Observer
     */
    void registerResponseInvalidPlayerEventObserver(Observer<ResponseInvalidPlayerEvent> observer);

    /**
     * Register the observer for ResponseInvalidStateEvent in the related observable
     * @param observer The Observer
     */
    void registerResponseInvalidStateEventObserver(Observer<ResponseInvalidStateEvent> observer);

}
