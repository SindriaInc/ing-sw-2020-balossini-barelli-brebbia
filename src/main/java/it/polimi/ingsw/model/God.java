package it.polimi.ingsw.model;

import it.polimi.ingsw.model.abilities.IAbilities;

public class God {

    /**
     * The god's name
     */
    private final String name;

    /**
     * The god's card number
     */
    private final int id;

    /**
     * The god's card description
     */
    private final String description;

    /**
     * The god's card type
     */
    private final String type;

    public God(String name, int id, String description, String type) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    /**
     * Decorates the given player's abilities with the god effect
     * @param abilities Previous player's abilities
     * @return The decorated abilities
     */
    // TODO: Implement method
    public IAbilities applyAbilities(IAbilities abilities) { return null; }

    /**
     * Decorates the given player's abilities with the god effect on opponents
     * @param abilities Previous player' abilities
     * @return The decorated abilities
     */
    // TODO: Implement method
    public IAbilities applyOpponentAbilities(IAbilities abilities) { return null; }

}
