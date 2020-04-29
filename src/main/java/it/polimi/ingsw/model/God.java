package it.polimi.ingsw.model;

import it.polimi.ingsw.model.abilities.AbilitiesDecorator;
import it.polimi.ingsw.model.abilities.IAbilities;

import java.util.List;
import java.util.Map;

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
     * The god's title
     */
    private final String title;

    /**
     * The god's card description
     */
    private final String description;

    /**
     * The god's card type
     */
    private final String type;

    /**
     * The list of decorators to apply, each class is mapped to a boolean that is true if the decorator must be applied to an opponent
     */
    private final Map<Class<? extends AbilitiesDecorator>, Boolean> effects;

    public God(String name, int id, String title, String description, String type, Map<Class<? extends AbilitiesDecorator>, Boolean> effects) {
        this.name = name;
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;

        this.effects = effects;
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

    /**
     * Decorates the given player's abilities with the god effect
     * @param abilities Previous player's abilities
     * @return The decorated abilities
     */
    public IAbilities applyAbilities(IAbilities abilities) {
        try {
            return doApplyAbilities(abilities);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            throw new IllegalStateException("Unable to instantiate decorators");
        }
    }

    /**
     * Decorates the given player's abilities with the god effect on opponents
     * @param abilities Previous player' abilities
     * @return The decorated abilities
     */
    public IAbilities applyOpponentAbilities(IAbilities abilities, Player originalPlayer) {
        try {
            return doApplyOpponentAbilities(abilities, originalPlayer);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            throw new IllegalStateException("Unable to instantiate decorators");
        }
    }

    private IAbilities doApplyAbilities(IAbilities abilities) throws ReflectiveOperationException {
        for (Map.Entry<Class<? extends AbilitiesDecorator>, Boolean> entry : effects.entrySet()) {
            // Skip opponent decorators
            if (entry.getValue()) {
                continue;
            }

            Class<? extends AbilitiesDecorator> decoratorClass = entry.getKey();
            abilities = decoratorClass.getDeclaredConstructor(IAbilities.class)
                    .newInstance(abilities);
        }

        return abilities;
    }


    private IAbilities doApplyOpponentAbilities(IAbilities abilities, Player originalPlayer) throws ReflectiveOperationException {
        for (Map.Entry<Class<? extends AbilitiesDecorator>, Boolean> entry : effects.entrySet()) {
            // Skip non-opponent decorators
            if (!entry.getValue()) {
                continue;
            }

            Class<? extends AbilitiesDecorator> decoratorClass = entry.getKey();
            abilities = decoratorClass.getDeclaredConstructor(IAbilities.class, List.class)
                    .newInstance(abilities, originalPlayer.getWorkers());
        }

        return abilities;
    }

}
