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

    private final String god;

    public PlayerChooseGodEvent(String player, String god) {
        super(player);

        this.god = god;
    }

    public String getGod() {
        return god;
    }

    @Override
    public void accept(ModelEventProvider provider) {
        provider.getPlayerChooseGodEventObservable().notifyObservers(this);
    }

    @Override
    public void accept(ViewEventProvider provider) {
        provider.getPlayerChooseGodEventObservable().notifyObservers(this);
    }

}