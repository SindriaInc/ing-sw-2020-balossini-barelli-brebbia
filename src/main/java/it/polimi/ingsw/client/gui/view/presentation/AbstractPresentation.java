package it.polimi.ingsw.client.gui.view.presentation;

import it.polimi.ingsw.client.gui.GuiAssets;
import it.polimi.ingsw.client.gui.GuiConstants;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Presentation for a state of the game
 */
public abstract class AbstractPresentation {

    /**
     * The assets
     */
    private final GuiAssets assets;

    /**
     * Class constructor, set the assets
     * @param assets The assets
     */
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
        bindCover(region.widthProperty(), region.heightProperty(), view);
    }

    /**
     * Bind the ImageView width and height such that it behaves like a cover image
     * @param width The container width
     * @param height The container height
     * @param view The ImageView
     */
    public void bindCover(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height, ImageView view) {
        double aspectRatio = view.getImage().getWidth() / view.getImage().getHeight();
        //view.setPreserveRatio(true);

        view.fitWidthProperty().bind(Bindings.createDoubleBinding(() -> {
            if (width.get() / height.get() > aspectRatio) {
                return width.get();
            } else {
                return height.get() * aspectRatio;
            }
        }, width, height));

        view.fitHeightProperty().bind(Bindings.createDoubleBinding(() -> {
            if (width.get() / height.get() <= aspectRatio) {
                return height.get();
            } else {
                return width.get() / aspectRatio;
            }
        }, width, height));
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
        button.setBackground(null);
        button.setFont(getAssets().getInputButtonFont());

        buttonImage.setPreserveRatio(true);
        buttonImage.setFitHeight(GuiConstants.INPUT_BUTTON_HEIGHT);

        styleShadow(buttonImage, pane);
    }

    /**
     * Set a drop shadow on an image view
     * @param imageView The image view
     * @param container The image container
     */
    public void styleShadow(ImageView imageView, Node container) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(15.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0.25, 0.25, 0.25));
        container.setOnMouseEntered(event -> imageView.setEffect(dropShadow));
        container.setOnMouseExited(event -> imageView.setEffect(null));
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

    /**
     * Style a button
     * @param component The button
     */
    public void styleButton(Button component) {
        component.setTextAlignment(TextAlignment.CENTER);

        Image image = getAssets().getImage(GuiAssets.Images.CREATE_ROOM_BUTTON);
        BackgroundSize backgroundSize = new BackgroundSize(1.0, 1.0, true, true, false , false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        component.setBackground(background);

        component.setFont(assets.getFont());
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(15.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0.2, 0.2, 0.2));
        component.setOnMouseEntered(event -> component.setEffect(dropShadow));
        component.setOnMouseExited(event -> component.setEffect(null));
    }

}
