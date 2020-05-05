package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.common.event.PlayerChallengerSelectGodsEvent;
import it.polimi.ingsw.common.event.PlayerChooseGodEvent;
import it.polimi.ingsw.common.event.PlayerTurnStartEvent;
import it.polimi.ingsw.common.event.request.RequestPlayerChallengerSelectGodsEvent;
import it.polimi.ingsw.common.event.request.RequestPlayerChooseGodEvent;
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

    public PreGodsGame(ModelEventProvider provider, Board board, List<Player> players, int maxWorkers, List<God> gods) {
        super(provider, board, players);

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

        getModelEventProvider().getPlayerTurnStartEventObservable().notifyObservers(
                new PlayerTurnStartEvent(getCurrentPlayer().getName())
        );
        getModelEventProvider().getPlayerRequestChallengerSelectGodsEventObservable().notifyObservers(
                new RequestPlayerChallengerSelectGodsEvent(getCurrentPlayer().getName(), getAvailableGods(), getSelectGodsCount())
        );
    }

    @Override
    public Game.ModelResponse selectGods(List<String> gods) {
        if (phase != Phase.CHALLENGER_SELECT_GODS) {
            // Unable to select gods in this phase
            return Game.ModelResponse.INVALID_STATE;
        }

        if (!checkCanSelectGods(gods)) {
            // Invalid god list
            return Game.ModelResponse.INVALID_PARAMS;
        }

        List<God> modelGods = new ArrayList<>();
        for (String god : gods) {
            modelGods.add(getGodByName(god));
        }

        availableGods.clear();
        availableGods.addAll(modelGods);

        getModelEventProvider().getChallengerSelectGodsEventObservable().notifyObservers(
                new PlayerChallengerSelectGodsEvent(getCurrentPlayer().getName(), List.copyOf(gods))
        );

        phase = Phase.PLAYER_SELECT_GOD;

        // Notify the first player to choose a god
        Player player = getCurrentPlayer();
        getModelEventProvider().getPlayerTurnStartEventObservable().notifyObservers(
                new PlayerTurnStartEvent(player.getName())
        );
        getModelEventProvider().getRequestPlayerChooseGodEventObservable().notifyObservers(
                new RequestPlayerChooseGodEvent(player.getName(), getAvailableGods())
        );

        return Game.ModelResponse.ALLOW;
    }

    @Override
    public Game.ModelResponse chooseGod(String god) {
        if (phase != Phase.PLAYER_SELECT_GOD) {
            // Unable to choose a god in this phase
            return Game.ModelResponse.INVALID_STATE;
        }

        God modelGod = getGodByName(god);

        if (modelGod == null) {
            // Unavailable god selected
            return Game.ModelResponse.INVALID_PARAMS;
        }

        Player player = getCurrentPlayer();
        player.applyGod(modelGod);

        for (Player other : getPlayers()) {
            if (other.equals(player)) {
                continue;
            }

            other.applyOpponentGod(modelGod, player);
        }

        availableGods.remove(modelGod);

        getModelEventProvider().getPlayerChooseGodEventObservable().notifyObservers(
                new PlayerChooseGodEvent(player.getName(), god)
        );
        return Game.ModelResponse.ALLOW;
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
        getModelEventProvider().getPlayerTurnStartEventObservable().notifyObservers(
                new PlayerTurnStartEvent(currentPlayer.getName())
        );
        getModelEventProvider().getRequestPlayerChooseGodEventObservable().notifyObservers(
                new RequestPlayerChooseGodEvent(currentPlayer.getName(), getAvailableGods())
        );
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

        return new PreWorkersGame(getModelEventProvider(), getBoard(), getPlayers(), maxWorkers, true);
    }

    /**
     * Get the list of available gods
     * If the selection has not been made the list will contain every god configured
     * Otherwise, the list will contain only the remaining gods that can be picked by the current player
     *
     * @return The List of gods
     */
    private List<String> getAvailableGods() {
        return toStringList(availableGods);
    }

    /**
     * Get the number of cards to be selected
     */
    private int getSelectGodsCount() {
        return getPlayers().size();
    }

    /**
     * Check if the gods provided can be selected
     * @param gods The list of the chosen god cards
     * @return true if the selection is valid
     */
    private boolean checkCanSelectGods(List<String> gods) {
        if (new HashSet<>(gods).size() != gods.size()) {
            return false; // No duplicates
        }

        if (!toStringList(availableGods).containsAll(gods)) {
            return false;
        }

        return gods.size() == getSelectGodsCount();
    }

    private List<String> toStringList(List<God> gods) {
        List<String> list = new ArrayList<>();

        for (God god : gods) {
            list.add(god.getName());
        }

        return list;
    }

    private God getGodByName(String god) {
        for (God modelGod : availableGods) {
            if (modelGod.getName().equals(god)) {
                return modelGod;
            }
        }

        return null;
    }

}
