package it.polimi.ingsw.client.gui.view.presentation;

import it.polimi.ingsw.client.gui.GuiAssets;
import it.polimi.ingsw.client.gui.GuiConstants;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.Map;

public class FieldsPresentation extends AbstractPresentation {

    public FieldsPresentation(GuiAssets assets) {
        super(assets);
    }

    public StackPane generatePresentation(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height,
                                          Map<Label, TextField> inputs,
                                          Button action
    ) {
        for (Map.Entry<Label, TextField> entry : inputs.entrySet()) {
            style(entry.getKey());
            style(entry.getValue());
        }

        style(action);

        action.setMaxWidth(Double.MAX_VALUE);

        ImageView logo = new ImageView(getAssets().getImage(GuiAssets.Images.LOGO));
        logo.setPreserveRatio(true);

        GridPane pane = new GridPane();
        pane.setVgap(GuiConstants.DEFAULT_SPACING);
        pane.setAlignment(Pos.CENTER);

        pane.add(logo, 0, 0);
        GridPane.setColumnSpan(logo, 2);

        int row = 1;
        for (Map.Entry<Label, TextField> entry : inputs.entrySet()) {
            pane.add(entry.getKey(), 0, row);
            pane.add(entry.getValue(), 1, row);
            row++;
        }

        pane.add(action, 1, 3);
        GridPane.setFillWidth(action, true);

        ImageView background = new ImageView(getAssets().getImage(GuiAssets.Images.BACKGROUND_MAIN));
        StackPane root = new StackPane(background, pane);
        bindCover(root, background);

        logo.fitWidthProperty().bind(Bindings.createDoubleBinding(() -> {
            double ratio = logo.getImage().getWidth() / logo.getImage().getHeight();
            double logoWidth = width.doubleValue() - (width.doubleValue() / 2);

            if (logoWidth > GuiConstants.LOGO_MAX_WIDTH) {
                return (double) GuiConstants.LOGO_MAX_WIDTH;
            }

            double logoHeight = logoWidth / ratio;
            double maxHeight = height.doubleValue() - 170;

            if (logoHeight > maxHeight) {
                return maxHeight * ratio;
            }

            return logoWidth;
        }, width, height));

        for (Label label : inputs.keySet()) {
            label.setMinWidth(GuiConstants.INPUT_LABEL_WIDTH);
        }

        return root;
    }

}
