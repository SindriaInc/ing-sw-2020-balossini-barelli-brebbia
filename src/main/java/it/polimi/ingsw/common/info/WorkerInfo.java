package it.polimi.ingsw.common.info;

public class WorkerInfo {

    private final int id;
    private final String owner;
    private final Coordinates position;

    public WorkerInfo(int id, String owner, Coordinates position) {
        this.id = id;
        this.owner = owner;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public Coordinates getPosition() {
        return position;
    }

}
