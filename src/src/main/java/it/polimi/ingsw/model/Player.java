package it.polimi.ingsw.model;

import it.polimi.ingsw.model.abilities.DefaultAbilities;
import it.polimi.ingsw.model.abilities.IAbilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The class representing a player, which can choose a god and
 * has a list of workers that can do actions during his turn.
 * The player is identified by his nickname and age
 */
public class Player {

    /**
     * The user's nickname
     */
    private final String name;

    /**
     * The user's age, used to determine
     */
    private final int age;

    /**
     * List containing the workers, starts empty, gets filled after the user chooses their positions
     */
    private final List<Worker> workers = new ArrayList<>();

    /**
     * The player's god, may be null
     */
    private God god;

    /**
     * The player abilities
     */
    private IAbilities abilities;

    /**
     * Class constructor
     * @param name The player's name
     * @param age The player's age
     */
    public Player(String name, int age) {
        this.name = name;
        this.age = age;

        abilities = new DefaultAbilities();
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    /**
     * Obtains the list of the player's workers
     * @return The players workers
     */
    public List<Worker> getWorkers() {
        return List.copyOf(workers);
    }

    /**
     * Adds a worker to the list of workers
     * @param worker The Worker
     * @throws IllegalArgumentException If the worker is already present
     */
    public void addWorker(Worker worker) throws IllegalArgumentException {
        if (workers.contains(worker)) {
            throw new IllegalArgumentException("Worker already present");
        }

        workers.add(worker);
    }

    /**
     * @see IAbilities#checkHasWon(Turn)
     */
    public boolean checkHasWon(Turn turn) {
        return abilities.checkHasWon(turn);
    }

    /**
     * @see IAbilities#checkCanMove(Turn, Cell)
     */
    public boolean checkCanMove(Turn turn, Cell cell) {
        return abilities.checkCanMove(turn, cell);
    }

    /**
     * @see IAbilities#doMove(Turn, Cell)
     */
    public void doMove(Turn turn, Cell cell) {
        abilities.doMove(turn, cell);
    }

    /**
     * @see IAbilities#checkCanBuildBlock(Turn, Cell)
     */
    public boolean checkCanBuildBlock(Turn turn, Cell cell) {
        return abilities.checkCanBuildBlock(turn, cell);
    }

    /**
     * @see IAbilities#doBuildBlock(Turn, Cell)
     */
    public void doBuildBlock(Turn turn, Cell cell) {
        abilities.doBuildBlock(turn, cell);
    }

    /**
     * @see IAbilities#checkCanBuildDome(Turn, Cell)
     */
    public boolean checkCanBuildDome(Turn turn, Cell cell) {
        return abilities.checkCanBuildDome(turn, cell);
    }

    /**
     * @see IAbilities#doBuildDome(Turn, Cell)
     */
    public void doBuildDome(Turn turn, Cell cell) {
        abilities.doBuildDome(turn, cell);
    }

    /**
     * @see IAbilities#checkCanForce(Turn, Worker, Cell)
     */
    public boolean checkCanForce(Turn turn, Worker worker, Cell cell) {
        return abilities.checkCanForce(turn, worker, cell);
    }

    /**
     * @see IAbilities#doForce(Turn, Worker, Cell)
     */
    public void doForce(Turn turn, Worker worker, Cell cell) {
        abilities.doForce(turn, worker, cell);
    }

    /**
     * Obtains the god associated to the player (if present)
     * @return The player's god (if present)
     */
    public Optional<God> getGod() {
        return Optional.ofNullable(god);
    }

    /**
     * Applies the god's effect to the player
     * @param god The god chosen by the player
     */
    public void applyGod(God god) {
        this.god = god;
        abilities = god.applyAbilities(abilities);
    }

    /**
     * Applies the god's effect to the other players (if the god effects apply to them)
     * @param god The god chosen by the player
     */
    public void applyOpponentGod(God god, Player owner) {
        abilities = god.applyOpponentAbilities(abilities, owner);
    }

    /**
     * Reset the player abilities, restoring only the god's effect if present
     */
    public void resetAbilities() {
        abilities = new DefaultAbilities();

        Optional<God> god = getGod();

        if (god.isEmpty()) {
            return;
        }

        applyGod(god.get());
    }

}
