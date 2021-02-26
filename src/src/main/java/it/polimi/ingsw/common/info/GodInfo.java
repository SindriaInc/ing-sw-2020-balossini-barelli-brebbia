package it.polimi.ingsw.common.info;

import java.util.Objects;

/**
 * A simplified version of a <code>God</code>, only containing serializable fields
 * To be used to share information via events
 */
public class GodInfo {

    /**
     * The god name
     */
    private final String name;

    /**
     * The god id
     */
    private final int id;

    /**
     * The god title
     */
    private final String title;

    /**
     * The god description
     */
    private final String description;

    /**
     * The god type
     */
    private final String type;

    /**
     * Class constructor
     *
     * @param name The god name
     * @param id The god id
     * @param title The god title
     * @param description The god description
     * @param type The god type
     */
    public GodInfo(String name, int id, String title, String description, String type) {
        this.name = name;
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        GodInfo godInfo = (GodInfo) object;
        return id == godInfo.id &&
                name.equals(godInfo.name) &&
                title.equals(godInfo.title) &&
                description.equals(godInfo.description) &&
                type.equals(godInfo.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, title, description, type);
    }
}
