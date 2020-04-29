package it.polimi.ingsw.common.events.requests;

import java.util.List;

public class RequestChallengerSelectGodsEvent {

    private final List<String> gods;

    private final int selectedGodsCount;

    public RequestChallengerSelectGodsEvent(List<String> gods, int selectedGodsCount) {
        this.gods = gods;
        this.selectedGodsCount = selectedGodsCount;
    }

    public List<String> getGods() {
        return gods;
    }

    public int getSelectedGodsCount() {
        return selectedGodsCount;
    }

}
