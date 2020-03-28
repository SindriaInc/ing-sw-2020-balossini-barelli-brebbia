package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Turn {

    /**
     * List of blocks placed by the player in the current turn
     */
    private List<Cell> blocksPlaced = new ArrayList<>();

    /**
     * List of domes placed by the player in the current turn
     */
    private List<Cell> domesPlaced = new ArrayList<>();


    public List<Cell> getBlocksPlaced() {
        return blocksPlaced;
    }

    public List<Cell> getDomesPlaced() {
        return domesPlaced;
    }
}
