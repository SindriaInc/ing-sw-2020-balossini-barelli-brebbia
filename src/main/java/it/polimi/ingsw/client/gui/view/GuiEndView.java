package it.polimi.ingsw.client.gui.view;

import it.polimi.ingsw.client.clientstates.AbstractClientState;
import it.polimi.ingsw.client.clientstates.EndState;
import it.polimi.ingsw.client.data.EndData;
import it.polimi.ingsw.client.gui.GuiConstants;
import it.polimi.ingsw.client.gui.GuiAssets;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class GuiEndView extends AbstractGuiView {

    private final EndState state;

    public GuiEndView(EndState state, GuiAssets images) {
        super(images);

        this.state = state;
    }

    public Parent generateView(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height) {
        EndData data = state.getData();

        String winner = data.getWinner();

        Text winText = new Text();

        if (winner == null) {
            winText.setText("Your opponent left the game!");
        } else if (winner.equals(state.getData().getName())) {
            winText.setText("Congratulations! You've won!");
        } else {
            winText.setText(winner + " has won the game");
        }

        // Presentation

        GridPane pane = new GridPane();
        pane.setVgap(GuiConstants.DEFAULT_SPACING);
        pane.setHgap(GuiConstants.DEFAULT_SPACING);
        pane.setAlignment(Pos.CENTER);

        pane.add(winText, 0, 1);
        return pane;
    }

    @Override
    public AbstractClientState getState() {
        return state;
    }

}
