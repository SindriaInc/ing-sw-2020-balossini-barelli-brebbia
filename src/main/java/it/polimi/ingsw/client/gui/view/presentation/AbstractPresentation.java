package it.polimi.ingsw.client.gui.view.presentation;

import it.polimi.ingsw.client.gui.GuiAssets;
import it.polimi.ingsw.client.gui.GuiConstants;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class AbstractPresentation {

    private final GuiAssets assets;

    public AbstractPresentation(GuiAssets assets) {
        this.assets = assets;
    }

    public GuiAssets getAssets() {
        return assets;
    }

    /**
     * Bind the ImageView width and height such that it behaves like a cover image
     * @param region The containing Region
     * @param view The ImageView
     */
    public void bindCover(Region region, ImageView view) {
        view.setPreserveRatio(true);

        view.fitWidthProperty().bind(Bindings.createDoubleBinding(() -> {
            if (region.getWidth() / region.getHeight() > view.getImage().getWidth() / view.getImage().getHeight()) {
                return region.getWidth();
            } else {
                return view.getImage().getWidth();
            }
        }, region.widthProperty(), region.heightProperty()));

        view.fitHeightProperty().bind(Bindings.createDoubleBinding(() -> {
            if (region.getWidth() / region.getHeight() <= view.getImage().getWidth() / view.getImage().getHeight()) {
                return region.getHeight();
            } else {
                return view.getImage().getHeight();
            }
        }, region.widthProperty(), region.heightProperty()));
    }

    /**
     * Style a simple label
     * @param label The label
     */
    public void style(Label label) {
        label.setFont(getAssets().getFont());
    }

    /**
     * Style an input label on the left of an input box
     * @param component The label
     */
    public void styleInputLabel(Label component) {
        CornerRadii corner = new CornerRadii(
                GuiConstants.INPUT_CORNER_RADII,
                0,
                0,
                GuiConstants.INPUT_CORNER_RADII,
                false
        );

        component.setAlignment(Pos.CENTER);
        component.setMinHeight(GuiConstants.INPUT_HEIGHT);
        component.setBackground(new Background(new BackgroundFill(Color.rgb(224, 224, 224), corner, Insets.EMPTY)));
        component.setFont(assets.getFont());
    }

    /**
     * Style a text field
     * @param component The field
     */
    public void style(TextField component) {
        CornerRadii corner = new CornerRadii(
                0,
                GuiConstants.INPUT_CORNER_RADII,
                GuiConstants.INPUT_CORNER_RADII,
                0,
                false
        );

        component.setMinHeight(GuiConstants.INPUT_HEIGHT);
        component.setBackground(new Background(new BackgroundFill(Color.WHITE, corner, Insets.EMPTY)));
        component.setFont(assets.getFont());
    }

    /**
     * Style a button
     * @param component The field
     */
    public void style(Button component) {
        CornerRadii corner = new CornerRadii(
                GuiConstants.INPUT_CORNER_RADII,
                GuiConstants.INPUT_CORNER_RADII,
                GuiConstants.INPUT_CORNER_RADII,
                GuiConstants.INPUT_CORNER_RADII,
                false
        );

        Background sleeping = new Background(new BackgroundFill(Color.WHITE, corner, Insets.EMPTY));
        Background hover = new Background(new BackgroundFill(Color.rgb(220, 220, 255), corner, Insets.EMPTY));
        Background click = new Background(new BackgroundFill(Color.rgb(204, 204, 255), corner, Insets.EMPTY));

        component.setMinHeight(GuiConstants.INPUT_HEIGHT);
        component.setBackground(sleeping);
        component.setOnMouseEntered(event -> component.setBackground(hover));
        component.setOnMouseExited(event -> component.setBackground(sleeping));
        component.setOnMousePressed(event -> component.setBackground(click));
        component.setFont(assets.getFont());
    }

    /**
     * Style a button with a background image
     * @param button The field
     * @param buttonImage The field background
     */
    public void style(ImageView buttonImage, Button button, StackPane pane) {
        CornerRadii corner = new CornerRadii(
                GuiConstants.INPUT_CORNER_RADII,
                GuiConstants.INPUT_CORNER_RADII,
                GuiConstants.INPUT_CORNER_RADII,
                GuiConstants.INPUT_CORNER_RADII,
                false
        );

        button.setBackground(null);
        button.setFont(getAssets().getInputButtonFont());

        buttonImage.setPreserveRatio(true);
        buttonImage.setFitHeight(GuiConstants.INPUT_BUTTON_HEIGHT);

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(+0.25);
        pane.setOnMouseEntered(event -> buttonImage.setEffect(colorAdjust));
        pane.setOnMouseExited(event -> buttonImage.setEffect(null));
    }

    /**
     * Style a text component
     * @param component The text
     */
    public void style(Text component) {
        component.setFont(assets.getFont());
    }

    /**
     * Style a big text component
     * @param component The text
     */
    public void styleTitle(Text component) {
        component.setFont(assets.getTitleFont());
    }

}
