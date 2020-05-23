package it.polimi.ingsw.model;

import it.polimi.ingsw.common.event.request.RequestPlayerPingEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LobbyTest {

    private Deck deck;
    private Lobby lobby;
    private ModelEventProvider modelEventProvider;

    private int lobbyUpdateCount;
    private int lobbyRoomUpdateCount;
    private int lobbyGameStartEventCount;

    private int pingCount;

    @BeforeEach
    void setUp() {
        deck = new Deck(List.of());
        lobby = new Lobby(deck);
        modelEventProvider = lobby.getModelEventProvider();
    }

    @Test
    void checkCorrectLogin(){
        modelEventProvider.registerLobbyUpdateEventObserver(event -> lobbyUpdateCount++);
        assertEquals(lobbyUpdateCount, 0);
        assertEquals(ModelResponse.ALLOW, lobby.login("Carlo",22));
        lobby.login("Carlo",22);
        assertEquals(lobbyUpdateCount, 1);
        assertEquals((ModelResponse.INVALID_PARAMS), lobby.login("Carlo", 43));
        lobby.login("Paola", 34);
        assertEquals(lobbyUpdateCount, 3);
    }

    @Test
    void checkRoomCreationAndJoining(){
        modelEventProvider.registerLobbyUpdateEventObserver(event -> lobbyUpdateCount++);
        modelEventProvider.registerLobbyRoomUpdateEventObserver(event -> lobbyRoomUpdateCount++);
        modelEventProvider.registerLobbyGameStartEventObserver(event -> lobbyGameStartEventCount++);
        assertEquals(lobbyUpdateCount, 0);
        assertEquals(lobbyRoomUpdateCount, 0);
        assertEquals(lobbyGameStartEventCount, 0);
        lobby.login("Carlo",22);
        assertEquals(lobbyUpdateCount, 1);
        assertEquals(lobbyRoomUpdateCount, 0);
        lobby.login("Paola", 34);
        assertEquals(lobbyUpdateCount, 3);
        assertEquals(lobbyRoomUpdateCount, 0);
        assertEquals(ModelResponse.INVALID_STATE, lobby.createRoom("Carlo", 1, true));
        assertEquals(ModelResponse.INVALID_STATE, lobby.createRoom("Paola", 10, false));
        lobby.createRoom("Carlo", 3, true);
        assertEquals(lobbyUpdateCount, 4);
        assertEquals(lobbyRoomUpdateCount, 1);
        assertEquals(ModelResponse.INVALID_STATE, lobby.createRoom("Carlo", 3, false));
        assertEquals(ModelResponse.INVALID_PARAMS, lobby.joinRoom("Paola", "Giulio"));
        lobby.joinRoom("Paola", "Carlo");
        assertEquals(ModelResponse.INVALID_STATE, lobby.joinRoom("Paola", "Carlo"));
        lobby.login("Marco", 12);
        lobby.joinRoom("Marco", "Carlo");
        assertEquals(lobbyGameStartEventCount,3);
    }

    @Test
    void checkCorrectLogout(){
        modelEventProvider.registerLobbyUpdateEventObserver(event -> lobbyUpdateCount++);
        modelEventProvider.registerLobbyRoomUpdateEventObserver(event -> lobbyRoomUpdateCount++);
        modelEventProvider.registerLobbyGameStartEventObserver(event -> lobbyGameStartEventCount++);
        //AssertCount not verified
        lobby.login("Mario", 89);
        lobby.login("Laura", 45);
        lobby.login("Dario", 23);
        lobby.login("Giulia", 16);
        lobby.login("Giorgio", 45);
        lobby.login("Paolo", 5);
        lobby.login("Luca", 90);
        assertEquals(ModelResponse.ALLOW, lobby.logout("Mario"));
        lobby.createRoom("Dario", 3, true);
        lobby.joinRoom("Laura", "Dario");
        assertEquals(ModelResponse.ALLOW, lobby.logout("Dario"));
        assertEquals(ModelResponse.INVALID_STATE, lobby.createRoom("Laura", 2, true));// The player was moved to a new room
        assertEquals(ModelResponse.ALLOW, lobby.joinRoom("Giulia", "Laura"));
        lobby.joinRoom("Giulia", "Laura");
        assertEquals(ModelResponse.ALLOW, lobby.logout("Giulia"));
        lobby.joinRoom("Giorgio", "Laura");
        lobby.joinRoom("Luca", "Laura");
        assertEquals(ModelResponse.INVALID_PARAMS, lobby.joinRoom("Paolo", "Laura"));
        assertEquals(ModelResponse.INVALID_PARAMS, lobby.login("Luca",3));
        assertEquals(ModelResponse.ALLOW, lobby.logout("Luca"));
    }

    @Test
    void playerCheckerTest(){
        assertNotNull(lobby.getPlayerChecker());
    }

    @Test
    void checkProvider() {
        modelEventProvider.registerRequestPlayerPingEventObserver((event) -> {
            pingCount++;
        });

        modelEventProvider.getRequestPlayerPingEventObservable().notifyObservers(
                new RequestPlayerPingEvent("Example")
        );

        assertEquals(1, pingCount);
    }

}