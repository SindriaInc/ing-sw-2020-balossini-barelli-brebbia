package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private static final int WORKERS = 2;

    /**
     * The user's nickname
     */
    private String name;

    /**
     * The user's age, used to determine
     */
    private int age;

    /**
     * List containing the workers, starts empty, gets filled after the user chooses their positions
     */
    private List<Worker> workers = new ArrayList<>();

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

    public void addWorker(Worker worker) {
        workers.add(worker);
    }

    // TODO: Implement methods
    public boolean checkHasWon() { return false; }

}
