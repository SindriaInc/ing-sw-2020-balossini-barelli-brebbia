package it.polimi.ingsw.client.gui.view.game;

import it.polimi.ingsw.client.AbstractClientViewer;
import it.polimi.ingsw.client.clientstates.GameState;
import it.polimi.ingsw.client.data.GameData;
import it.polimi.ingsw.client.data.request.ChooseGodData;
import it.polimi.ingsw.client.data.request.SelectFirstData;
import it.polimi.ingsw.client.data.request.SelectGodsData;
import it.polimi.ingsw.client.gui.GuiAssets;
import it.polimi.ingsw.client.gui.GuiConstants;
import it.polimi.ingsw.client.gui.view.component.GodBox;
import it.polimi.ingsw.client.gui.view.presentation.AbstractPresentation;
import it.polimi.ingsw.client.gui.view.presentation.GodsPresentation;
import it.polimi.ingsw.common.info.GodInfo;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Create the gui rendering of the gods view
 */
public class GuiGodsView extends AbstractGameView {

    private final ObservableList<String> selectedGods = FXCollections.observableList(new ArrayList<>());

    /**
     * Class constructor, set state and assets
     * @param state The state
     * @param images The assets
     */
    public GuiGodsView(GameState state, GuiAssets images) {
        super(state, images);
    }

    /**
     * @see it.polimi.ingsw.client.gui.view.AbstractGuiView#generateView(ReadOnlyDoubleProperty, ReadOnlyDoubleProperty)
     */
    @Override
    public Parent generateView(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height) {
        GameData data = getState().getData();
        GodsPresentation presentation = new GodsPresentation(getAssets());

        selectedGods.clear();

        StackPane bottom = new StackPane();

        Optional<SelectGodsData> selectGodsData = data.getSelectGodsData();
        Optional<ChooseGodData> chooseGodData = data.getChooseGodData();
        Optional<SelectFirstData> selectFirstData = data.getSelectFirstData();

        Text action = new Text();

        FlowPane godsView = new FlowPane();
        godsView.setHgap(GuiConstants.DEFAULT_SPACING);
        godsView.setVgap(GuiConstants.DEFAULT_SPACING);
        godsView.setPadding(new Insets(GuiConstants.DEFAULT_SPACING*2));
        godsView.setAlignment(Pos.CENTER);

        Button selectButton;
        StackPane center;

        if (selectFirstData.isPresent()) {
            action = new Text("You're the Challenger!\nSelect the first player to spawn workers");
            action.setFont(getAssets().getTitleFont());
            action.setTextAlignment(TextAlignment.CENTER);

            List<Button> buttons = new ArrayList<>();

            for (String player : selectFirstData.get().getAvailablePlayers()) {
                Button button = new Button();
                presentation.styleButton(button);
                button.setText(player);
                button.setOnAction((event) -> onSelectFirst(player));
                buttons.add(button);
            }

            // Presentation

            GridPane gridPane = new GridPane();
            gridPane.add(action, 0, 0);
            GridPane.setHalignment(action, HPos.CENTER);
            gridPane.setAlignment(Pos.CENTER);

            int index = 1;
            for (Button button : buttons) {
                button.setMaxWidth(100);
                button.setAlignment(Pos.CENTER);
                gridPane.add(button, 0, index++);
                GridPane.setHalignment(button, HPos.CENTER);
            }

            gridPane.setVgap(GuiConstants.DEFAULT_SPACING);
            gridPane.setMinWidth(GuiConstants.INFO_MIN_SIZE);

            center = new StackPane(gridPane);
            center.setAlignment(Pos.CENTER);
        } else if (selectGodsData.isPresent()) {
            action.setText("You're the Challenger!\nSelect the gods that will be available for everyone to use");

            selectButton = new Button();

            List<GodBox> godComponentsList = new ArrayList<>();
            List<ImageView> godImageList = new ArrayList<>();
            for (GodInfo god : selectGodsData.get().getAvailableGods()) {
                StringBinding text = Bindings.createStringBinding(
                        () -> selectedGods.contains(god.getName()) ? "Deselect" : "Select",
                        selectedGods
                );

                BooleanBinding enabled = Bindings.createBooleanBinding(
                        () -> selectedGods.contains(god.getName()) || selectedGods.size() < selectGodsData.get().getSelectGodsCount(),
                        selectedGods
                );

                ImageView godCard = getCard(god);
                godImageList.add(godCard);

                Text finalAction = action;
                EventHandler<ActionEvent> handler = (ignored) -> {
                    onGodToggle(god.getName());
                    if (text.get().equals("Deselect")) {
                        showDoneButton(selectButton, finalAction, bottom);
                    }
                };
                GodBox godBox = new GodBox(god, text, enabled, handler, getAssets().getFont());

                godComponentsList.add(godBox);
                presentation.styleButton(godBox.getButton());

                godCard.setOnMouseClicked(event -> {
                    if (godComponentsList.get(godImageList.indexOf(godCard)).equals(bottom.getChildren().get(1))) {
                        showDoneButton(selectButton, finalAction, bottom);
                    } else {
                        showGodInfo(godBox, bottom);
                    }
                });

                godsView.getChildren().add(godCard);
            }
            showDoneButton(selectButton, action, bottom);

            BooleanBinding selectEnabled = Bindings.createBooleanBinding(
                    () -> selectedGods.size() == selectGodsData.get().getSelectGodsCount(),
                    selectedGods
            );

            selectButton.setText("Done");
            selectButton.setOnAction(this::onGodToggle);
            selectButton.disableProperty().bind(selectEnabled.not());
            presentation.styleButton(selectButton);
            center = new StackPane(godsScroll(godsView));
        } else if (chooseGodData.isPresent()) {
            action.setText("Choose a god to use");
            showAction(action, bottom);

            StringBinding text = Bindings.createStringBinding(() -> "Choose");
            BooleanBinding enabled = Bindings.createBooleanBinding(() -> true);

            for (GodInfo god : chooseGodData.get().getAvailableGods()) {
                ImageView godCard = getCard(god);

                EventHandler<ActionEvent> handler = (ignored) -> onChoose(god.getName());
                GodBox godBox = new GodBox(god, text, enabled, handler, getAssets().getFont());
                presentation.styleButton(godBox.getButton());

                godCard.setOnMouseClicked(event -> showGodInfo(godBox, bottom));

                godsView.getChildren().add(godCard);
            }

            center = new StackPane(godsScroll(godsView));

        } else {
            action.setText("Waiting for the other players to complete their turn...");
            action.setTextAlignment(TextAlignment.CENTER);
            action.setFont(getAssets().getTitleFont());

            center = new StackPane(action);
        }

        // Presentation
        return presentation.generatePresentation(width, height, center, bottom);
    }

    /**
     * Handler for the choose button
     * @param name The god's name
     */
    private void onChoose(String name) {
        getState().acceptChooseGod(name);
    }

    /**
     * Handler for a god toggle button
     * @param ignored The action event
     */
    private void onGodToggle(ActionEvent ignored) {
        getState().acceptSelectGods(selectedGods);
    }

    /**
     * Handler for a god toggle button
     * @param name The god's name
     */
    private void onGodToggle(String name) {
        if (selectedGods.contains(name)) {
            selectedGods.remove(name);
            return;
        }

        selectedGods.add(name);
    }

    /**
     * The handler for a select first player button
     * @param player The player name
     */
    private void onSelectFirst(String player) {
        getState().acceptSelectFirst(player);
    }

    /**
     * Load a card image
     * @param god The card's god name
     * @return The image view of the card
     */
    private ImageView getCard(GodInfo god) {
        ImageView godCard = new ImageView(new Image(AbstractClientViewer.ASSETS_DIRECTORY + "image-card-" + god.getName().toLowerCase() + ".png"));
        godCard.setFitWidth(130);
        godCard.setPreserveRatio(true);
        new AbstractPresentation(getAssets()){}.styleShadow(godCard, godCard);

        return godCard;
    }

    /**
     * Show the god info in a GodBox on the bottom stack pane
     * @param godBox The god box
     * @param bottom The stack pane
     */
    private void showGodInfo(GodBox godBox, StackPane bottom) {
        removeChild(bottom);
        bottom.getChildren().add(godBox);
    }

    /**
     * Show the done button on the bottom stack pane
     * @param done The button
     * @param action The text
     * @param bottom The stack pane
     */
    private void showDoneButton(Button done, Text action, StackPane bottom) {
        removeChild(bottom);

        action.setFont(getAssets().getFont());
        action.setTextAlignment(TextAlignment.CENTER);
        VBox vBox= new VBox(action, done);
        vBox.setAlignment(Pos.CENTER);
        bottom.getChildren().add(vBox);
    }

    /**
     * Remove the child with index 1 from a stack pane
     * @param bottom The stack pane
     */
    private void removeChild(StackPane bottom) {
        try {
            bottom.getChildren().remove(1);
        } catch (IndexOutOfBoundsException ignored) {}
    }

    /**
     * Show the action text in a stack pane
     * @param action The text
     * @param bottom The stack pane
     */
    private void showAction(Text action, StackPane bottom) {
        action.setFont(getAssets().getFont());
        action.setTextAlignment(TextAlignment.CENTER);
        bottom.getChildren().add(action);
    }

    /**
     * Create a scroll pane to contain god cards flow pane
     * @param godsView The flow pane
     * @return A scroll pane containing the flow pane
     */
    private ScrollPane godsScroll(FlowPane godsView) {
        godsView.setBackground(Background.EMPTY);
        ScrollPane godsScroll = new ScrollPane(godsView);
        godsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        godsScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        godsScroll.setBackground(Background.EMPTY);
        godsScroll.setFitToWidth(true);

        return godsScroll;
    }

}
