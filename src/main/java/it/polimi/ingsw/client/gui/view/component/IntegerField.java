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

    public BooleanBinding validBinding() {
        return Bindings.createBooleanBinding(() -> getValue().isPresent(), textProperty());
    }

    public Optional<Integer> getValue() {
        return getValue(getText());
    }

    private Optional<Integer> getValue(String text) {
        try {
            return Optional.of(Integer.parseInt(text));
        } catch (NumberFormatException ignored) {
            return Optional.empty();
        }
    }

}
