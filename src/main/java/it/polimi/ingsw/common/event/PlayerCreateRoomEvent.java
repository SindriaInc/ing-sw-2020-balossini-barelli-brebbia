package it.polimi.ingsw.common.event;

public class PlayerCreateRoomEvent extends AbstractPlayerEvent {

    private final int maxPlayers;

    private final boolean simpleGame;

    public PlayerCreateRoomEvent(String player, int maxPlayers, boolean simpleGame) {
        super(player);
        this.maxPlayers = maxPlayers;
        this.simpleGame = simpleGame;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public boolean isSimpleGame() {
        return simpleGame;
    }

}
