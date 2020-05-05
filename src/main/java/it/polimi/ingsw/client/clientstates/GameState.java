package it.polimi.ingsw.client.clientstates;

import it.polimi.ingsw.client.clientstates.lobbystates.ChooseLobby;

public class GameState extends AbstractClientState {

    /**
     * The current state of game phase, implementing the available interactions
     */
    private AbstractClientState currentGameState;

    public GameState() {
        //TODO: game State pattern
    }

    @Override
    public AbstractClientState nextClientState() {
        return null;
    }
}
