package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.common.event.PlayerChallengerSelectFirstEvent;
import it.polimi.ingsw.common.event.PlayerChallengerSelectGodsEvent;
import it.polimi.ingsw.common.event.PlayerChooseGodEvent;
import it.polimi.ingsw.common.event.PlayerTurnStartEvent;
import it.polimi.ingsw.common.event.request.RequestPlayerChallengerSelectFirstEvent;
import it.polimi.ingsw.common.event.request.RequestPlayerChallengerSelectGodsEvent;
import it.polimi.ingsw.common.event.request.RequestPlayerChooseGodEvent;
import it.polimi.ingsw.common.info.GodInfo;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The class representing the state of game in which the challenger chooses the gods from the deck and players
 * choose from the challenger list a god to use in the game.
 * If the game is simple this phase is skipped.
 */
public class PreGodsGame extends AbstractGameState {

    private enum Phase {

        CHALLENGER_SELECT_GODS,
        PLAYER_SELECT_GOD,
        CHALLENGER_SELECT_FIRST

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

    /**
     * The first player index (refers to getPlayers())
     * The value is null until the challenger has selected the player that will start choosing workers
     */
    private Integer firstIndex;

    /**
     * Class constructor
     * @param provider The provider of the events
     * @param board The game's board
     * @param players The game's players
     * @param maxWorkers The max number of worker each player can spawn
     * @param gods The gods list for the game
     */
    public PreGodsGame(ModelEventProvider provider, Board board, List<Player> players, int maxWorkers, List<God> gods) {
        super(provider, board, players);

        this.availableGods.addAll(gods);
        this.maxWorkers = maxWorkers;

        Player challenger = getPlayers().get(0);
        phase = Phase.CHALLENGER_SELECT_GODS;

        List<Player> sortedPlayers = new ArrayList<>();
        for (Player player : getPlayers()) {
            if (player.equals(challenger)) {
                continue;
            }

            sortedPlayers.add(player);
        }

        sortedPlayers.add(challenger); // The challenger is the last player to select the workers
        challengerIndex = sortedPlayers.indexOf(challenger);

        sortPlayers(sortedPlayers);

        var event = new PlayerTurnStartEvent(getCurrentPlayer().getName());
        setReceivers(event);
        event.accept(getModelEventProvider());

        new RequestPlayerChallengerSelectGodsEvent(getCurrentPlayer().getName(), getAvailableGods(), getSelectGodsCount())
                .accept(getModelEventProvider());
    }

    /**
     * @see AbstractGameState#selectGods(List)
     */
    @Override
    public ModelResponse selectGods(List<String> gods) {
        if (phase != Phase.CHALLENGER_SELECT_GODS) {
            // Unable to select gods in this phase
            return ModelResponse.INVALID_STATE;
        }

        if (!checkCanSelectGods(gods)) {
            // Invalid god list
            return ModelResponse.INVALID_PARAMS;
        }

        List<God> modelGods = new ArrayList<>();
        for (String god : gods) {
            modelGods.add(getGodByName(god));
        }

        availableGods.clear();
        availableGods.addAll(modelGods);

        var event = new PlayerChallengerSelectGodsEvent(getCurrentPlayer().getName(), List.copyOf(gods));
        setReceivers(event);
        event.accept(getModelEventProvider());

        phase = Phase.PLAYER_SELECT_GOD;

        // Notify the first player to choose a god
        Player player = getCurrentPlayer();
        generateChooseRequests(player);

        return ModelResponse.ALLOW;
    }

    /**
     * @see AbstractGameState#chooseGod(String)
     */
    @Override
    public ModelResponse chooseGod(String god) {
        if (phase != Phase.PLAYER_SELECT_GOD) {
            // Unable to choose a god in this phase
            return ModelResponse.INVALID_STATE;
        }

        God modelGod = getGodByName(god);

        if (modelGod == null) {
            // Unavailable god selected
            return ModelResponse.INVALID_PARAMS;
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

        var event = new PlayerChooseGodEvent(player.getName(), god);
        setReceivers(event);
        event.accept(getModelEventProvider());

        Player next = getCurrentPlayer();

        if (next != null) {
            generateChooseRequests(next);
        } else {
            // Every player has chosen a god, ask the challenger to select the player

            phase = Phase.CHALLENGER_SELECT_FIRST;

            List<String> players = getPlayers().stream().map(Player::getName).collect(Collectors.toList());
            new RequestPlayerChallengerSelectFirstEvent(getCurrentPlayer().getName(), players)
                    .accept(getModelEventProvider());
        }

        return ModelResponse.ALLOW;
    }

    /**
     * @see AbstractGameState#selectFirst(String)
     */
    @Override
    public ModelResponse selectFirst(String player) {
        if (phase != Phase.CHALLENGER_SELECT_FIRST) {
            // Unable to select gods in this phase
            return ModelResponse.INVALID_STATE;
        }

        Integer optionalIndex = null;

        int index = 0;
        for (Player other : getPlayers()) {
            if (other.getName().equals(player)) {
                optionalIndex = index;
            }
            index++;
        }

        if (optionalIndex == null) {
            // Invalid player selected
            return ModelResponse.INVALID_PARAMS;
        }

        // The first player has been chosen correctly, nextState must return PreWorkersGame now
        firstIndex = optionalIndex;

        var event = new PlayerChallengerSelectFirstEvent(getCurrentPlayer().getName(), player);
        setReceivers(event);
        event.accept(getModelEventProvider());

        return ModelResponse.ALLOW;
    }

    /**
     * @see AbstractGameState#getCurrentPlayer()
     */
    @Override
    public Player getCurrentPlayer() {
        if (phase == Phase.CHALLENGER_SELECT_GODS || phase == Phase.CHALLENGER_SELECT_FIRST) {
            return getPlayers().get(challengerIndex);
        }

        Player currentPlayer = getPlayers().get(playerIndex);

        if (currentPlayer.getGod().isEmpty()) {
            return currentPlayer;
        }

        if (playerIndex + 1 >= getPlayers().size()) {
            // No other player needs to choose a god
            return null;
        }

        playerIndex++;
        return getPlayers().get(playerIndex);
    }

    /**
     * @see AbstractGameState#nextState()
     */
    @Override
    public AbstractGameState nextState() {
        if (!isDone()) {
            return this;
        }

        // Generate the new list of players, starting from the first player instead of the challenger
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < getPlayers().size(); i++) {
            Player next = getPlayers().get((i + firstIndex) % getPlayers().size());
            players.add(next);
        }

        return new PreWorkersGame(getModelEventProvider(), getBoard(), players, maxWorkers, true);
    }

    private boolean isDone() {
        return firstIndex != null;
    }

    /**
     * Get the list of available gods
     * If the selection has not been made the list will contain every god configured
     * Otherwise, the list will contain only the remaining gods that can be picked by the current player
     *
     * @return The gods available as a list of GodInfo
     */
    private List<GodInfo> getAvailableGods() {
        return toGodInfoList(availableGods);
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

    /**
     * Generates the choose requests of a player
     * @param player The player
     */
    private void generateChooseRequests(Player player) {
        var event = new PlayerTurnStartEvent(player.getName());
        setReceivers(event);
        event.accept(getModelEventProvider());

        new RequestPlayerChooseGodEvent(player.getName(), getAvailableGods())
                .accept(getModelEventProvider());
    }

    /**
     * Converts a list of gods in a list of string
     * @param gods The list of gods
     * @return A list of strings
     */
    private List<String> toStringList(List<God> gods) {
        List<String> list = new ArrayList<>();

        for (God god : gods) {
            list.add(god.getName());
        }

        return list;
    }
    /**
     * Generates a list of godinfo from a list of gods
     * @param gods The list of gods
     * @return A list of godinfo
     */
    private List<GodInfo> toGodInfoList(List<God> gods) {
        List<GodInfo> list = new ArrayList<>();

        for (God god : gods) {
            list.add(toGodInfo(god));
        }

        return list;
    }

    /**
     * Generates godinfo from a god
     * @param god The selected gods
     * @return The godinfo
     */
    private GodInfo toGodInfo(God god) {
        return new GodInfo(god.getName(), god.getId(), god.getTitle(), god.getDescription(), god.getType());
    }

    /**
     * Obtains a god from its name
     * @param god The god's name
     * @return The selected god
     */
    private God getGodByName(String god) {
        for (God modelGod : availableGods) {
            if (modelGod.getName().equals(god)) {
                return modelGod;
            }
        }

        return null;
    }

}
