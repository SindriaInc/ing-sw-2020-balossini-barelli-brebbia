package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.model.*;

import java.util.*;

public class PreGodsGame extends AbstractGameState {

    private enum Phase {

        CHALLENGER_SELECT_GODS,
        PLAYER_SELECT_GOD

    }

    /**
     * List of the god cards chosen for the current game
     */
    private final List<God> availableGods = new ArrayList<>();

    /**
     * The maximum number of workers per player
     */
    private final int maxWorkers;

    /**
     * The challenger index (refers to getPlayers())
     * The challenger is player who will select what gods are available
     */
    private final int challengerIndex;

    /**
     * The current player index (refers to getPlayers())
     */
    private int playerIndex;

    /**
     * The current phase of the state
     */
    private Phase phase;

    public PreGodsGame(Board board, List<Player> players, int maxWorkers, List<God> gods) {
        super(board, players);

        this.availableGods.addAll(gods);
        this.maxWorkers = maxWorkers;

        Player challenger = getPlayers().get(new Random().nextInt(getPlayers().size()));
        phase = Phase.CHALLENGER_SELECT_GODS;

        List<Player> sortedPlayers = new LinkedList<>();
        for (Player player : getPlayers()) {
            if (player.equals(challenger)) {
                continue;
            }

            sortedPlayers.add(player);
        }

        sortedPlayers.add(challenger); // The challenger is the last player to select the workers
        challengerIndex = sortedPlayers.indexOf(challenger);

        sortPlayers(sortedPlayers);
    }

    @Override
    public List<God> getAvailableGods() {
        return List.copyOf(availableGods);
    }

    @Override
    public int getSelectGodsCount() {
        return getPlayers().size();
    }

    @Override
    public boolean checkCanSelectGods(List<God> gods) {
        if (new HashSet<>(gods).size() != gods.size()) {
            return false; // No duplicates
        }

        if (!availableGods.containsAll(gods)) {
            return false;
        }

        return gods.size() == getSelectGodsCount();
    }

    @Override
    public void selectGods(List<God> gods) {
        if (phase != Phase.CHALLENGER_SELECT_GODS) {
            throw new IllegalStateException("Unable to select gods in this phase");
        }

        if (!checkCanSelectGods(gods)) {
            throw new IllegalArgumentException("Invalid god list");
        }

        availableGods.clear();
        availableGods.addAll(gods);

        phase = Phase.PLAYER_SELECT_GOD;
    }

    @Override
    public void chooseGod(God god) {
        if (phase != Phase.PLAYER_SELECT_GOD) {
            throw new IllegalStateException("Unable to select gods in this phase");
        }

        if (!getAvailableGods().contains(god)) {
            throw new IllegalArgumentException("Unavailable god selected");
        }

        Player player = getCurrentPlayer();
        player.applyGod(god);

        for (Player other : getPlayers()) {
            if (other.equals(player)) {
                continue;
            }

            other.applyOpponentGod(god, player);
        }

        availableGods.remove(god);
    }

    @Override
    public Player getCurrentPlayer() {
        if (phase == Phase.CHALLENGER_SELECT_GODS) {
            return getPlayers().get(challengerIndex);
        }

        Player currentPlayer = getPlayers().get(playerIndex);

        if (currentPlayer.getGod().isEmpty()) {
            return currentPlayer;
        }

        playerIndex++;
        return getPlayers().get(playerIndex);
    }

    @Override
    public AbstractGameState nextState() {
        if (phase == Phase.CHALLENGER_SELECT_GODS) {
            return this;
        }

        for (Player player : getPlayers()) {
            if (player.getGod().isEmpty()) {
                return this;
            }
        }

        return new PreWorkersGame(getBoard(), getPlayers(), maxWorkers, true);
    }

    @Override
    public boolean isEnded() {
        return false;
    }

}
