package it.polimi.ingsw.common.logging;

public enum LogLevel {

    DEBUG(0),
    INFO(1),
    WARNING(2),
    SEVERE(3);

    private final int level;

    LogLevel(int level) {
        this.level = level;
    }

    public boolean filter(LogLevel base) {
        return level >= base.level;
    }

}
