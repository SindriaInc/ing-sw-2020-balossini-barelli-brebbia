package it.polimi.ingsw.common.info;

import java.util.Objects;

public class CellInfo {

    private final int level;
    private final boolean doomed;

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
