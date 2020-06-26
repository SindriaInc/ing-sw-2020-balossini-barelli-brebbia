package it.polimi.ingsw.client.gui.view;

import it.polimi.ingsw.client.clientstates.AbstractClientState;
import it.polimi.ingsw.client.clientstates.EndState;
import it.polimi.ingsw.client.data.EndData;
import it.polimi.ingsw.client.gui.GuiConstants;
import it.polimi.ingsw.client.gui.GuiAssets;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class GuiEndView extends AbstractGuiView {

    private static final double COMPONENTS_HEIGHT = 200;
    private static final double MAX_GAMEOVER_HEIGHT = 600;

    private final EndState state;

    public GuiEndView(EndState state, GuiAssets images) {
        super(images);

        this.state = state;
    }

    public Parent generateView(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height) {
        EndData data = state.getData();

        String winner = data.getWinner();

        ImageView message;
        if (state.getData().getWinner()!=null && winner.equals(state.getData().getName())) {
            message = new ImageView(getAssets().getImage(GuiAssets.Images.WIN_MESSAGE));
        } else {
            message = new ImageView(getAssets().getImage(GuiAssets.Images.GAMEOVER_MESSAGE));
            if (winner == null) {
                winner = "An opponent has left.\nNobody";
            }
        }
        message.setPreserveRatio(true);

        // Presentation

        GridPane pane = new GridPane();
        pane.setVgap(GuiConstants.DEFAULT_SPACING);
        pane.setHgap(GuiConstants.DEFAULT_SPACING);
        pane.setAlignment(Pos.CENTER);

        pane.add(message, 0, 1);

        BackgroundImage backgroundImage;
        if (winner.equals((state.getData().getName()))) {
            backgroundImage = new BackgroundImage(getAssets().getImage(GuiAssets.Images.WIN_BACKGROUND), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                                                BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, false));
        } else {
            backgroundImage = new BackgroundImage(getAssets().getImage(GuiAssets.Images.GAMEOVER_BACKGROUND), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                                                BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, false));
            Text winText = new Text();
            winText.setFont(getAssets().getEndFont());
            winText.setText(winner + " has won the game");
            winText.setTextAlignment(TextAlignment.CENTER);
            GridPane.setHalignment(winText, HPos.CENTER);
            pane.add(winText, 0, 2);
        }

        pane.setAlignment(Pos.CENTER);
        GridPane.setHalignment(message, HPos.CENTER);
        pane.setBackground(new Background(backgroundImage));

        message.fitWidthProperty().bind(Bindings.createDoubleBinding(() -> {
            double ratio = message.getImage().getWidth() / message.getImage().getHeight();
            double messageWidth = width.doubleValue() - (width.doubleValue() / 4);
            double messageHeight = messageWidth / ratio;
            double maxHeight = height.doubleValue() - COMPONENTS_HEIGHT;

            if (messageHeight > MAX_GAMEOVER_HEIGHT && maxHeight > MAX_GAMEOVER_HEIGHT) {
                return MAX_GAMEOVER_HEIGHT * ratio;
            }

            if (messageHeight > maxHeight) {
                return maxHeight * ratio;
            }

            return messageWidth;
        }, width, height));

        return pane;
    }

    @Override
    public AbstractClientState getState() {
        return state;
    }

}
