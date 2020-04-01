package it.polimi.ingsw.model;

import it.polimi.ingsw.model.abilities.DefaultAbilities;
import it.polimi.ingsw.model.abilities.IAbilities;

import java.util.ArrayList;
import java.util.List;

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
     * The player abilities
     */
    private IAbilities abilities;

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
     * Checks if the player meets any win condition
     * @return true if any win conditions is met
     */
    public boolean checkHasWon() {
        return abilities.checkHasWon(getWorkers());
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

}
