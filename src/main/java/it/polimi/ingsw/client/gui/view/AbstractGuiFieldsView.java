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

public abstract class AbstractGuiFieldsView extends AbstractGuiView {

    private Alert currentAlert;

    private final String stringLabel;
    private final String stringPrompt;
    private final String integerLabel;
    private final String integerPrompt;
    private final String buttonText;

    /**
     * The previously generated view, null before generateView is called
     */
    private Parent generatedView;

    public AbstractGuiFieldsView(GuiAssets images, String stringLabel, String stringPrompt, String integerLabel, String integerPrompt, String buttonText) {
        super(images);
        this.stringLabel = stringLabel;
        this.stringPrompt = stringPrompt;
        this.integerLabel = integerLabel;
        this.integerPrompt = integerPrompt;
        this.buttonText = buttonText;
    }

    @Override
    public void closeWindows() {
        if (currentAlert != null) {
            currentAlert.close();
            currentAlert = null;
        }
    }

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

    public abstract void onAction(String string, int integer);

}
