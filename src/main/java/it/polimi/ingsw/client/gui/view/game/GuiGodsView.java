package it.polimi.ingsw.client.gui.view.game;

import it.polimi.ingsw.client.clientstates.GameState;
import it.polimi.ingsw.client.data.GameData;
import it.polimi.ingsw.client.data.request.ChooseGodData;
import it.polimi.ingsw.client.data.request.SelectGodsData;
import it.polimi.ingsw.client.gui.GuiConstants;
import it.polimi.ingsw.client.gui.GuiAssets;
import it.polimi.ingsw.client.gui.view.component.GodBox;
import it.polimi.ingsw.common.info.GodInfo;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GuiGodsView extends AbstractGameView {

    private final ObservableList<String> selectedGods = FXCollections.observableList(new ArrayList<>());

    public GuiGodsView(GameState state, GuiAssets images) {
        super(state, images);
    }

    @Override
    public Parent generateView(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height) {
        GameData data = getState().getData();

        selectedGods.clear();

        Optional<SelectGodsData> selectGodsData = data.getSelectGodsData();
        Optional<ChooseGodData> chooseGodData = data.getChooseGodData();

        Text action = new Text();

        ListView<GodBox> godsView = new ListView<>();
        ObservableList<GodBox> godComponents;
        Button selectButton = null;

        if (selectGodsData.isPresent()) {
            action.setText("You're the Challenger!\nSelect the gods that will be available for everyone to use");

            List<GodBox> godComponentsList = new ArrayList<>();
            for (GodInfo god : selectGodsData.get().getAvailableGods()) {
                StringBinding text = Bindings.createStringBinding(
                        () -> selectedGods.contains(god.getName()) ? "Deselect" : "Select",
                        selectedGods
                );

                BooleanBinding enabled = Bindings.createBooleanBinding(
                        () -> selectedGods.contains(god.getName()) || selectedGods.size() < selectGodsData.get().getSelectGodsCount(),
                        selectedGods
                );

                godComponentsList.add(new GodBox(god, text, enabled, (ignored) -> onGodToggle(god.getName())));
            }
            godComponents = FXCollections.observableList(godComponentsList);

            BooleanBinding selectEnabled = Bindings.createBooleanBinding(
                    () -> selectedGods.size() == selectGodsData.get().getSelectGodsCount(),
                    selectedGods
            );

            selectButton = new Button();
            selectButton.setText("Done");
            selectButton.setOnAction(this::onGodToggle);
            selectButton.disableProperty().bind(selectEnabled.not());
        } else if (chooseGodData.isPresent()) {
            action.setText("Choose a god to use");

            StringBinding text = Bindings.createStringBinding(() -> "Choose");
            BooleanBinding enabled = Bindings.createBooleanBinding(() -> true);

            godComponents = FXCollections.observableList(
                    chooseGodData.get().getAvailableGods().stream()
                            .map(god -> new GodBox(god, text, enabled, (ignored) -> onChoose(god.getName())))
                            .collect(Collectors.toList())
            );
        } else {
            action.setText("Waiting for the other players to complete their turn...");

            BorderPane pane = new BorderPane();
            pane.setCenter(action);
            return pane;
        }

        godsView.setItems(godComponents);

        // Presentation

        BorderPane pane = new BorderPane();
        pane.setTop(action);
        pane.setCenter(godsView);

        BorderPane.setMargin(action, new Insets(GuiConstants.DEFAULT_SPACING));
        BorderPane.setAlignment(action, Pos.CENTER);

        if (selectButton != null) {
            pane.setBottom(selectButton);

            BorderPane.setMargin(selectButton, new Insets(GuiConstants.DEFAULT_SPACING));
            BorderPane.setAlignment(selectButton, Pos.CENTER);
        }

        return pane;
    }

    private void onChoose(String name) {
        getState().acceptChooseGod(name);
    }

    private void onGodToggle(ActionEvent ignored) {
        getState().acceptSelectGods(selectedGods);
    }

    private void onGodToggle(String name) {
        if (selectedGods.contains(name)) {
            selectedGods.remove(name);
            return;
        }

        selectedGods.add(name);
    }

}
