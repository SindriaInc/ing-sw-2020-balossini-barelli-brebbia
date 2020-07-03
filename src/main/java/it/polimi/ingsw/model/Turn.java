package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The class representing a turn of the game, during which a worker
 *  can move, build a block, force or build a dome
 */
public class Turn {

    /**
     * The enumeration representing the type of action of the worker
     */
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

    /**
     * The class representing the action that
     * a worker can do during a turn
     */
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
     */
    private final List<Action> actions = new ArrayList<>();

    /**
     * List of workers that can not win on this turn
     */
    private final List<Worker> bannedWinWorkers = new ArrayList<>();

    /**
     * List of workers who were moved in the last action, in addition to the worker used in this turn
     * This list is used to notify about forces added by decorators
     */
    private final List<Worker> movedWorkers = new ArrayList<>();

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

    /**
     * Identifies the workers that can win
     * @return A list of the workers candidate to win
     */
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

    /**
     * Obtains a copy of the list which contains the other workers of the turn
     * @return A list of the other workers
     */
    public List<Worker> getOtherWorkers() {
        return List.copyOf(otherWorkers.keySet());
    }

    /**
     * Identifies the cells neighbours to the worker cell
     * @param  cell The worker cell
     * @return A list of the neighbour cells
     */
    public List<Cell> getNeighbours(Cell cell) {
        return getNeighbours.apply(cell);
    }

    /**
     * Checks if the cell is a perimeter cell
     * @param  cell The cell
     * @return True if the cell is a perimeter cell
     */
    public boolean isPerimeterSpace(Cell cell) {
        return isPerimeterSpace.test(cell);
    }

    /**
     * Checks if two worker have the same player
     * @param  worker The selected worker
     * @return True if the two workers have the same owner
     */
    public boolean hasSamePlayer(Worker worker) {
        return otherWorkers.get(worker);
    }

    /**
     * Obtains the workers moves during a turn
     * @return The list of moves
     */
    public List<Cell> getMoves() {
        return actions.stream()
                .filter(action -> action.getType() == ActionType.MOVEMENT)
                .map(Action::getCell)
                .collect(Collectors.toList());
    }

    /**
     * Obtains the worker builds during a turn
     * @return The list of builds
     */
    public List<Cell> getBuilds() {
        return actions.stream()
                .filter(action -> action.getType().isBuild())
                .map(Action::getCell)
                .collect(Collectors.toList());
    }
    /**
     * Obtains the block placed during a turn
     * @return The list of block placed
     */
    public List<Cell> getBlocksPlaced() {
        return actions.stream()
                .filter(action -> action.getType() == ActionType.BLOCK)
                .map(Action::getCell)
                .collect(Collectors.toList());
    }
    /**
     * Obtains the dome placed during a turn
     * @return The list of dome placed
     */
    public List<Cell> getDomesPlaced() {
        return actions.stream()
                .filter(action -> action.getType() == ActionType.DOME)
                .map(Action::getCell)
                .collect(Collectors.toList());
    }
    /**
     * Obtains the forces during a turn
     * @return The list of forces
     */
    public List<Action> getForces() {
        return actions.stream()
                .filter(action -> action.getType() == ActionType.FORCE)
                .collect(Collectors.toList());
    }

    /**
     * Obtains the standard actions during a turn
     * @return The list of standard actions
     */
    public List<Action> getStandardActions() {
        return actions.stream()
                .filter(action -> action.getType().isStandard())
                .collect(Collectors.toList());
    }

    /**
     * Obtains a copy of the list which contains the moved workers
     * @return A list of the moved workers
     */
    public List<Worker> getMovedWorkers() {
        return List.copyOf(movedWorkers);
    }

    /**
     * Adds a cell to the list of movements
     * @param cell The cell of the movement
     */
    public void addMovement(Cell cell) {
        actions.add(new Action(ActionType.MOVEMENT, null, cell));
    }

    /**
     * Adds a cell to the block placed
     * @param cell The cell where the block is placed
     */
    public void addBlockPlaced(Cell cell) {
        actions.add(new Action(ActionType.BLOCK, null, cell));
    }

    /**
     * Adds a cell to the dome placed
     * @param cell The cell where the dome is placed
     */
    public void addDomePlaced(Cell cell) {
        actions.add(new Action(ActionType.DOME, null, cell));
    }

    /**
     * Adds a cell to the list of force
     * @param cell The cell of the force
     */
    public void addForce(Worker target, Cell cell) {
        actions.add(new Action(ActionType.FORCE, target, cell));
    }

    /**
     * Adds a worker to the list of workers that can not win
     * @param worker The banned worker
     */
    public void addBannedWinWorker(Worker worker) {
        bannedWinWorkers.add(worker);
    }

    /**
     * Adds a worker to the list of moved worker
     * @param worker The worker that had moved
     */
    public void addMovedWorker(Worker worker) {
        movedWorkers.add(worker);
    }

    /**
     * Clears the moved workers list
     */
    public void clearMovedWorkers() {
        movedWorkers.clear();
    }

    public Cell getStartingCell() {
        return startingCell;
    }

}
