package it.polimi.ingsw.common.info;

import java.util.Objects;

/**
 * A simplified version of a <code>Cell</code>, only containing serializable fields
 * To be used to share information via events
 */
public class CellInfo {

    /**
     * The level of blocks built in the cell
     */
    private final int level;

    /**
     * Whether or not the cell has a dome on top of the blocks
     */
    private final boolean doomed;

    /**
     * Class constructor
     *
     * @param level The level of the blocks
     * @param doomed Whether the cell has a dome
     */
    public CellInfo(int level, boolean doomed) {
        this.level = level;
        this.doomed = doomed;
    }

    public int getLevel() {
        return level;
    }

    public boolean isDoomed() {
        return doomed;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        CellInfo cellInfo = (CellInfo) object;
        return level == cellInfo.level &&
                doomed == cellInfo.doomed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, doomed);
    }

}
