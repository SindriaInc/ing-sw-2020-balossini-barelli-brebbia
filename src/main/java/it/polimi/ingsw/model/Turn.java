package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Turn {

    public enum ActionType {

        MOVEMENT(false, true),
        BLOCK(true, true),
        DOME(true, true),
        FORCE(false, false);

        private final boolean build;
        private final boolean standard;

        ActionType(boolean build, boolean standard) {
            this.build = build;
            this.standard = standard;
        }

        public boolean isBuild() {
            return build;
        }

        public boolean isStandard() {
            return standard;
        }

    }

    public static class Action {

        private final ActionType type;
        private final Worker target;
        private final Cell cell;

        public Action(ActionType type, Worker target, Cell cell) {
            this.type = type;
            this.target = target;
            this.cell = cell;
        }

        public ActionType getType() {
            return type;
        }

        public Worker getTarget() {
            return target;
        }

        public Cell getCell() {
            return cell;
        }

    }

    /**
     * The worker used in this turn
     */
    private final Worker worker;

    /**
     * The other workers on the board in this turn
     */
    private final Map<Worker, Boolean> otherWorkers;

    /**
     * The neighbouring cells
     */
    private final Function<Cell, List<Cell>> getNeighbours;

    /**
     * Whether or not a cell is part of the perimeter
     */
    private final Predicate<Cell> isPerimeterSpace;

    /**
     * The starting cell
     */
    private final Cell startingCell;

    /**
     * List of actions done by the worker
     * The list is guaranteed to be sorted by insertion order
     */
    private final List<Action> actions = new LinkedList<>();


    /**
     * List of workers that can not win on this turn
     */
    private final List<Worker> bannedWinWorkers = new ArrayList<>();

    /**
     * Instantiates a Turn
     * @param worker The Worker used during the Turn
     * @param otherWorkers The other Workers present, the boolean represents whether or not they belong to the same player
     * @param getNeighbours List of neighboring to a cell
     */
    public Turn(Worker worker, Map<Worker, Boolean> otherWorkers, Function<Cell, List<Cell>> getNeighbours, Predicate<Cell> isPerimeterSpace) {
        this.worker = worker;
        this.otherWorkers = Map.copyOf(otherWorkers);
        this.getNeighbours = getNeighbours;
        this.isPerimeterSpace = isPerimeterSpace;
        this.startingCell = worker.getCell();
    }

    public Worker getWorker() {
        return worker;
    }

    public List<Worker> getCandidateWinWorkers() {
        List<Worker> sameWorkers = new ArrayList<>();
        sameWorkers.add(worker);

        for (Map.Entry<Worker, Boolean> other : otherWorkers.entrySet()) {
            if (other.getValue()) {
                sameWorkers.add(other.getKey());
            }
        }

        sameWorkers.removeAll(bannedWinWorkers);
        return sameWorkers;
    }

    public List<Worker> getOtherWorkers() {
        return List.copyOf(otherWorkers.keySet());
    }

    public List<Cell> getNeighbours(Cell cell) {
        return getNeighbours.apply(cell);
    }

    public boolean isPerimeterSpace(Cell cell) {
        return isPerimeterSpace.test(cell);
    }

    public boolean hasSamePlayer(Worker worker) {
        return otherWorkers.get(worker);
    }

    public List<Cell> getMoves() {
        return actions.stream()
                .filter(action -> action.getType() == ActionType.MOVEMENT)
                .map(Action::getCell)
                .collect(Collectors.toList());
    }

    public List<Cell> getBuilds() {
        return actions.stream()
                .filter(action -> action.getType().isBuild())
                .map(Action::getCell)
                .collect(Collectors.toList());
    }

    public List<Cell> getBlocksPlaced() {
        return actions.stream()
                .filter(action -> action.getType() == ActionType.BLOCK)
                .map(Action::getCell)
                .collect(Collectors.toList());
    }

    public List<Cell> getDomesPlaced() {
        return actions.stream()
                .filter(action -> action.getType() == ActionType.DOME)
                .map(Action::getCell)
                .collect(Collectors.toList());
    }

    public List<Action> getForces() {
        return actions.stream()
                .filter(action -> action.getType() == ActionType.FORCE)
                .collect(Collectors.toList());
    }

    public List<Action> getStandardActions() {
        return actions.stream()
                .filter(action -> action.getType().isStandard())
                .collect(Collectors.toList());
    }

    public void addMovement(Cell cell) {
        actions.add(new Action(ActionType.MOVEMENT, null, cell));
    }

    public void addBlockPlaced(Cell cell) {
        actions.add(new Action(ActionType.BLOCK, null, cell));
    }

    public void addDomePlaced(Cell cell) {
        actions.add(new Action(ActionType.DOME, null, cell));
    }

    public void addForce(Worker target, Cell cell) {
        actions.add(new Action(ActionType.FORCE, target, cell));
    }

    public void addBannedWinWorker(Worker worker) {
        bannedWinWorkers.add(worker);
    }

    public Cell getStartingCell() {
        return startingCell;
    }

}
