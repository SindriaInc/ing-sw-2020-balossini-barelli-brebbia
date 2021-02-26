package it.polimi.ingsw.common;

import it.polimi.ingsw.common.event.response.ResponseInvalidLoginEvent;
import it.polimi.ingsw.common.event.response.ResponseInvalidParametersEvent;
import it.polimi.ingsw.common.event.response.ResponseInvalidPlayerEvent;
import it.polimi.ingsw.common.event.response.ResponseInvalidStateEvent;

/**
 * The provider that handles the registration of response events
 *
 * Response events are events sent by a controller (server)
 */
public interface IResponseEventProvider {

    /**
     * Register the observer for ResponseInvalidLoginEvent in the related observable
     * @param observer The Observer
     */
    void registerResponseInvalidLoginEventObserver(Observer<ResponseInvalidLoginEvent> observer);

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
