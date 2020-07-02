package it.polimi.ingsw.client.gui.view.dialog;

import it.polimi.ingsw.client.gui.GuiConstants;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class CreateRoomDialog extends Dialog<Pair<Integer, Boolean>> {

    public CreateRoomDialog(int minGamePlayers, int maxGamePlayers) {
        setTitle("Create a new room...");

        TextField maxPlayers = new TextField();
        maxPlayers.setPromptText("Maximum players");
        maxPlayers.setTextFormatter(new TextFormatter<>(change -> {
            String text = change.getControlNewText();

            if (text.isEmpty()) {
                return change;
            }

            try {
                Integer.parseInt(text);
            } catch (NumberFormatException ignored) {
                return null;
            }

            return change;
        }));

        CheckBox simpleGame = new CheckBox();
        simpleGame.setText("Simple game");

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Disable the ok button if max players is not set
        BooleanProperty disableProperty = getDialogPane().lookupButton(ButtonType.OK).disableProperty();
        disableProperty.bind(Bindings.createBooleanBinding(() -> {
            Integer max = parseInteger(maxPlayers.getText());

            if (max == null) {
                // Disable if there's no number
                return true;
            }

            // Disable if the number is too high or too low
            return max < minGamePlayers || max > maxGamePlayers;
        }, maxPlayers.textProperty()));

        // Return "max players" and "is simple" when the dialog is closed
        setResultConverter(button -> {
            if (button.getButtonData() != ButtonBar.ButtonData.OK_DONE) {
                return null;
            }

            Integer maxPlayersValue = parseInteger(maxPlayers.getText());

            if (maxPlayersValue == null) {
                return null;
            }

            return new Pair<>(maxPlayersValue, simpleGame.isSelected());
        });

        // Presentation

        GridPane pane = new GridPane();
        pane.setHgap(GuiConstants.DEFAULT_SPACING);
        pane.setVgap(GuiConstants.DEFAULT_SPACING);

        pane.add(maxPlayers, 0, 0);
        pane.add(simpleGame, 1, 0);

        getDialogPane().setContent(pane);
    }

    /**
     * Parse a string turning into an integer, if possible
     * @param text The string
     * @return The int value, if exists, otherwise null
     */
    private Integer parseInteger(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

}
