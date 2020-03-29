package it.polimi.ingsw.model;

import it.polimi.ingsw.model.abilities.IAbilities;

import java.util.ArrayList;
import java.util.List;

public class Player {

    public final static String DEFAULT_NAME = "gInton1c";
    public final static int DEFAULT_AGE = 42;
    private static final int WORKERS = 2;

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
     * @see IAbilities#checkHasWon()
     */
    public boolean checkHasWon() {
        return abilities.checkHasWon();
    }

    /**
     * @see IAbilities#checkCanMove(Worker, Cell)
     */
    public boolean checkCanMove(Worker worker, Cell cell) {
        return abilities.checkCanMove(worker, cell);
    }

    /**
     * @see IAbilities#doMove(Worker, Cell)
     */
    public void doMove(Worker worker, Cell cell) {
        abilities.doMove(worker, cell);
    }

    /**
     * @see IAbilities#checkCanBuildBlock(Worker, Cell)
     */
    public boolean checkCanBuildBlock(Worker worker, Cell cell) {
        return abilities.checkCanBuildBlock(worker, cell);
    }

    /**
     * @see IAbilities#doBuildBlock(Worker, Cell)
     */
    public void doBuildBlock(Worker worker, Cell cell) {
        abilities.doBuildBlock(worker, cell);
    }

    /**
     * @see IAbilities#checkCanBuildDome(Worker, Cell)
     */
    public boolean checkCanBuildDome(Worker worker, Cell cell) {
        return abilities.checkCanBuildDome(worker, cell);
    }

    /**
     * @see IAbilities#doBuildDome(Worker, Cell)
     */
    public void doBuildDome(Worker worker, Cell cell) {
        abilities.doBuildDome(worker, cell);
    }

}
