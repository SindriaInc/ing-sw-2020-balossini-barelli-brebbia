package it.polimi.ingsw.client.gui.view.component;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.Optional;

public class IntegerField extends TextField {

    public IntegerField() {
        setTextFormatter(new TextFormatter<>(change -> {
            String text = change.getControlNewText();

            if (text.isEmpty()) {
                return change;
            }

            if (getValue(text).isPresent()) {
                return change;
            }

            return null;
        }));
    }

    /**
     * Create a binding allowing IntegerField to contain only integer values
     * @return The binding
     */
    public BooleanBinding validBinding() {
        return Bindings.createBooleanBinding(() -> getValue().isPresent(), textProperty());
    }

    /**
     * Get integer value in the field
     * @return The value
     */
    public Optional<Integer> getValue() {
        return getValue(getText());
    }

    /**
     * Get the integer value out of a text
     * @param text The text
     * @return The value
     */
    private Optional<Integer> getValue(String text) {
        try {
            return Optional.of(Integer.parseInt(text));
        } catch (NumberFormatException ignored) {
            return Optional.empty();
        }
    }

}
