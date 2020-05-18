package it.polimi.ingsw.client.clientstates;

import it.polimi.ingsw.client.ClientData;
import it.polimi.ingsw.client.message.ErrorMessage;
import it.polimi.ingsw.common.event.AbstractEvent;
import it.polimi.ingsw.common.event.PlayerPingEvent;

import java.util.Optional;
import java.util.function.Consumer;

public abstract class AbstractClientState {

    /**
     * The player name
     */
    private String playerName;

    /**
     * The function used to send a message to server
     */
    private Consumer<AbstractEvent> send;

    /**
     * Read log the gamer in
     * @return The gamer's data
     */
    public AADataTypes.GamerData login() {
        return null;
    }

    /**
     * Starts a socket connection with server
     * @param ip The ip address
     * @param port The port
     */
    public void connectToServer(String ip, int port) {}

    /**
     * Read new lobby data
     * @return The new lobby data
     */
    public AADataTypes.LobbyData readLobbyData () {
        return null;
    }

    /**
     * Execute the message
     * @param message The message
     */
    public abstract AADataTypes.Response onCommand(String message);

    /**
     * Obtain the next state of the client
     * Calling this method repeatedly should not result in a different state unless other methods got called
     * @return The AbstractClientState
     */
    public abstract AbstractClientState nextClientState();

    public void onConnect() {}

    /**
     * Respond to a ping
     */
    public void onRequestPlayerPingEvent(AbstractEvent event) {
        send.accept(new PlayerPingEvent(playerName));
    }

    /**
     * Update the lobby
     */
    public void onLobbyUpdateEvent(AbstractEvent event) {}

    /**
     * Update the room
     */
    public void onLobbyRoomUpdateEvent(AbstractEvent event) {}

    /**
     * Starts the room's game
     */
    public void onLobbyGameStartEvent(AbstractEvent event) {};

    /**
     * Starts this client's player turn
     */
    public void onPlayerTurnStartEvent(AbstractEvent event) {};

    /**
     * Make the challenger select gods
     */
    public void onRequestPlayerChallengerSelectGodsEvent(AbstractEvent event) {};

    /**
     * Make a player choose a god
     */
    public void onRequestPlayerChooseGodEvent(AbstractEvent event) {};

    /**
     * Spawn a worker
     */
    public void onRequestWorkerSpawnEvent(AbstractEvent event) {};

    /**
     * Move a worker
     */
    public void onRequestWorkerMoveEvent(AbstractEvent event) {};

    /**
     * Build a block
     */
    public void onRequestWorkerBuildBlockEvent(AbstractEvent event) {};

    /**
     * Build a dome
     */
    public void onRequestWorkerBuildDomeEvent(AbstractEvent event) {};

    /**
     * End own turn
     */
    public void onRequestPlayerEndTurnEvent(AbstractEvent event) {};

    /**
     * Show the gods chosen by the challenger
     */
    public void onPlayerChallengerSelectGodsEvent(AbstractEvent event) {};

    /**
     * Show the god chosen by another player
     */
    public void onPlayerChooseGodEvent(AbstractEvent event) {};

    /**
     * Spawn an opponent's worker
     */
    public void onWorkerSpawnEvent(AbstractEvent event) {};

    /**
     * Move an opponent's worker
     */
    public void onWorkerMoveEvent(AbstractEvent event) {};

    /**
     * Build a block placed by an opponent
     */
    public void onWorkerBuildBlockEvent(AbstractEvent event) {};

    /**
     * Build a dome placed by an opponent
     */
    public void onWorkerBuildDomeEvent(AbstractEvent event) {};

    /**
     * Make a player win
     */
    public void onPlayerWinEvent(AbstractEvent event) {};

    /**
     * Make a player lose
     */
    public void onPlayerLoseEvent(AbstractEvent event) {};

    /**
     * Handle an invalid parameters response
     */
    public void onResponseInvalidParametersEvent(AbstractEvent event) {};

    /**
     * Handle an invalid player response
     */
    public void onResponseInvalidPlayerEvent(AbstractEvent event) {};

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setSend(Consumer<AbstractEvent> send) {
        this.send = send;
    }
}
