package it.polimi.ingsw.client.gui.view.presentation;

import it.polimi.ingsw.client.gui.GuiAssets;
import it.polimi.ingsw.client.gui.GuiConstants;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.Map;

/**
 * The field presentation
 */
public class FieldsPresentation extends AbstractPresentation {

    private static final double COMPONENTS_HEIGHT = 170;

    /**
     * Class constructor, set assets
     * @param assets The assets
     */
    public FieldsPresentation(GuiAssets assets) {
        super(assets);
    }

    /**
     * Generate a field presentation
     * @param width The width
     * @param height The height
     * @param inputs The input text fields
     * @param action The action button
     * @param buttonImage The button image
     * @param connect The stack pane containing action and button image
     * @return The generated stack pane
     */
    public StackPane generatePresentation(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height,
                                          Map<Label, TextField> inputs,
                                          Button action, ImageView buttonImage, StackPane connect
    ) {
        for (Map.Entry<Label, TextField> entry : inputs.entrySet()) {
            styleInputLabel(entry.getKey());
            style(entry.getValue());
        }

        action.setMaxWidth(Double.MAX_VALUE);

        style(buttonImage, action, connect);

        ImageView logo = new ImageView(getAssets().getImage(GuiAssets.Images.LOGO_SUNSET));
        logo.setPreserveRatio(true);

        GridPane pane = new GridPane();
        pane.setVgap(GuiConstants.DEFAULT_SPACING);
        pane.setAlignment(Pos.CENTER);

        pane.add(logo, 0, 0);
        GridPane.setColumnSpan(logo, 2);
        GridPane.setHalignment(logo, HPos.CENTER);

        int row = 1;
        for (Map.Entry<Label, TextField> entry : inputs.entrySet()) {
            pane.add(entry.getKey(), 0, row);
            pane.add(entry.getValue(), 1, row);
            row++;
        }

        pane.add(connect, 1, 3);
        GridPane.setFillWidth(connect, true);

        ImageView background = new ImageView(getAssets().getImage(GuiAssets.Images.BACKGROUND_MAIN_SUNSET));
        StackPane root = new StackPane(background, pane);
        bindCover(root, background);

        logo.fitWidthProperty().bind(Bindings.createDoubleBinding(() -> {
            double ratio = logo.getImage().getWidth() / logo.getImage().getHeight();
            double logoWidth = width.doubleValue() - (width.doubleValue() / 2);
            double logoHeight = logoWidth / ratio;
            double maxHeight = height.doubleValue() - COMPONENTS_HEIGHT;

            if (logoWidth > GuiConstants.LOGO_MAX_WIDTH) {
                logoWidth = GuiConstants.LOGO_MAX_WIDTH;
            }

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
