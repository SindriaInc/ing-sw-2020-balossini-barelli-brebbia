package it.polimi.ingsw.client.socket;

public interface IInboundMessageReader {

    void scheduleRead(String message);

}
