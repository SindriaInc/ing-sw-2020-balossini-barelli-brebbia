package it.polimi.ingsw.common.event;

import it.polimi.ingsw.model.ModelEventProvider;
import it.polimi.ingsw.view.ViewEventProvider;

/**
 * Event for the god choice by the player
 *
 * View -> Model
 * Model -> View
 */
public class PlayerChooseGodEvent extends AbstractPlayerEvent {

    /**
     * The god chosen by the player
     */
    private final String god;

    /**
     * Class constructor
     *
     * @param player The player that is choosing the god
     * @param god The chosen god
     */
    public PlayerChooseGodEvent(String player, String god) {
        super(player);

        this.god = god;
    }

    public String getGod() {
        return god;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ModelEventProvider provider) {
        provider.getPlayerChooseGodEventObservable().notifyObservers(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ViewEventProvider provider) {
        provider.getPlayerChooseGodEventObservable().notifyObservers(this);
    }

}