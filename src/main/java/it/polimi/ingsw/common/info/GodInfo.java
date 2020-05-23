package it.polimi.ingsw.common.info;

import java.util.Objects;

public class GodInfo {

    private final String name;
    private final int id;
    private final String title;
    private final String description;
    private final String type;

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
