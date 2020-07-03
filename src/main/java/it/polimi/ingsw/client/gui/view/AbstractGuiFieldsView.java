package it.polimi.ingsw.client.gui.view;

import it.polimi.ingsw.client.gui.GuiAssets;
import it.polimi.ingsw.client.gui.view.component.IntegerField;
import it.polimi.ingsw.client.gui.view.presentation.FieldsPresentation;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The view of the fields
 */
public abstract class AbstractGuiFieldsView extends AbstractGuiView {

    /**
     * The current alert
     */
    private Alert currentAlert;

    /**
     * The text field label
     */
    private final String stringLabel;

    /**
     * The text field prompt text
     */
    private final String stringPrompt;

    /**
     * The integer field label
     */
    private final String integerLabel;

    /**
     * The integer field prompt text
     */
    private final String integerPrompt;

    /**
     * The button text
     */
    private final String buttonText;

    /**
     * The previously generated view, null before generateView is called
     */
    private Parent generatedView;

    /**
     * Abstract class constructor, set labels, prompt texts and assets
     * @param images The assets
     * @param stringLabel The text label
     * @param stringPrompt The text prompt string
     * @param integerLabel The integer label
     * @param integerPrompt The integer prompt text
     * @param buttonText The button text
     */
    public AbstractGuiFieldsView(GuiAssets images, String stringLabel, String stringPrompt, String integerLabel, String integerPrompt, String buttonText) {
        super(images);
        this.stringLabel = stringLabel;
        this.stringPrompt = stringPrompt;
        this.integerLabel = integerLabel;
        this.integerPrompt = integerPrompt;
        this.buttonText = buttonText;
    }

    /**
     * @see AbstractGuiView#closeWindows()
     */
    @Override
    public void closeWindows() {
        if (currentAlert != null) {
            currentAlert.close();
            currentAlert = null;
        }
    }

    /**
     * Generate a view of the fields
     * @param lastMessage The last message
     * @param width The width
     * @param height The height
     * @return The generated view
     */
    public Parent generateView(String lastMessage, ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height) {
        if (lastMessage != null) {
            if (currentAlert != null) {
                currentAlert.close();
            }

            currentAlert = new Alert(Alert.AlertType.WARNING);
            currentAlert.setContentText(lastMessage);
            currentAlert.show();
        }

        if (generatedView != null) {
            return generatedView;
        }

        Label stringLabel = new Label(this.stringLabel);
        TextField string = new TextField();
        string.setPromptText(stringPrompt);

        Label integerLabel = new Label(this.integerLabel);
        IntegerField integer = new IntegerField();
        integer.setPromptText(integerPrompt);

        Button button = new Button();
        button.setText(buttonText);
        ImageView buttonImage = new ImageView(getAssets().getImage(GuiAssets.Images.BUTTON_MAIN));

        StackPane connect = new StackPane(buttonImage, button);
        connect.disableProperty().bind(integer.validBinding().not());
        button.setOnAction(event -> onAction(string.getText(), integer.getValue().orElse(0)));
        buttonImage.setOnMouseClicked(event -> onAction(string.getText(), integer.getValue().orElse(0)));

        FieldsPresentation presentation = new FieldsPresentation(getAssets());

        Map<Label, TextField> inputs = new LinkedHashMap<>();
        inputs.put(stringLabel, string);
        inputs.put(integerLabel, integer);

        generatedView = presentation.generatePresentation(width, height, inputs, button, buttonImage, connect);
        return generatedView;
    }

    /**
     * The handler of the button in the view
     * @param string The string
     * @param integer The integer
     */
    public abstract void onAction(String string, int integer);

}
