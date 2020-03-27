package it.polimi.ingsw.model;

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
     * Modifies player's ability with the god ability when the player decides to activate it
     * @param abilities Previous player's ability
     * @return The decorated abilities
     */
    // TODO: Implement methods
    public Abilities applyAbilities(Abilities abilities){return null;}

    /**
     *
     * @param abilities Previous other players'ability
     * @return The decorated abilities
     */
    // TODO: Implement methods
    public Abilities applyOpponentAbilities(Abilities abilities){return null;}





}
