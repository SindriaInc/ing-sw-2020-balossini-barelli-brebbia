package it.polimi.ingsw.common.logging;

/**
 * The LogLevel represents the severity of a log message
 * Higher level numbers should be prioritized if logs need to be cut down
 */
public enum LogLevel {

    /**
     * The debug level is the lowest level, can be used to print messages useful for understanding how the application
     * is operating to better understand eventual issues
     */
    DEBUG(0),

    /**
     * The info level is the normal level, should be used to print messages useful to the user
     */
    INFO(1),

    /**
     * The warning level is an high level, should be used to print messages useful to the user that require their
     * immediate attention
     */
    WARNING(2),

    /**
     * The severe level is the highest level, should only be used to print critical messages in case of application
     * malfunctioning or security issues
     */
    SEVERE(3);

    /**
     * The integer value of the level
     */
    private final int level;

    /**
     * Enum constructor
     *
     * @param level The level
     */
    LogLevel(int level) {
        this.level = level;
    }

    /**
     * Filters this level, checking if it's higher than the base level
     *
     * @param base The base level
     * @return true if the current level is higher than the base level
     */
    public boolean filter(LogLevel base) {
        return level >= base.level;
    }

}
