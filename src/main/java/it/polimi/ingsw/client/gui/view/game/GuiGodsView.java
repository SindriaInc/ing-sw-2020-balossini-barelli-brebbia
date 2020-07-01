package it.polimi.ingsw.client.gui.view.game;

import it.polimi.ingsw.client.AbstractClientViewer;
import it.polimi.ingsw.client.clientstates.GameState;
import it.polimi.ingsw.client.data.GameData;
import it.polimi.ingsw.client.data.request.ChooseGodData;
import it.polimi.ingsw.client.data.request.SelectFirstData;
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
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

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

        Button selectButton = null;
        StackPane center;

        if (selectFirstData.isPresent()) {
            action = new Text("You're the Challenger!\nSelect the first player to spawn workers");
            action.setFont(getAssets().getTitleFont());
            action.setTextAlignment(TextAlignment.CENTER);

            List<Button> buttons = new ArrayList<>();

            for (String player : selectFirstData.get().getAvailablePlayers()) {
                Button button = new Button();
                style(button);
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

                Button finalSelectButton = selectButton;
                Text finalAction = action;
                EventHandler<ActionEvent> handler = (ignored) -> {
                    onGodToggle(god.getName());
                    if(text.get()=="Deselect") {
                        showDoneButton(finalSelectButton, finalAction, bottom);
                    }
                };
                GodBox godBox = new GodBox(god, text, enabled, handler, getAssets().getFont());

                godComponentsList.add(godBox);
                style(godBox.getButton());

                godCard.setOnMouseClicked(event -> {
                    if (godComponentsList.get(godImageList.indexOf(godCard)).equals(bottom.getChildren().get(1))) {
                        showDoneButton(finalSelectButton, finalAction, bottom);
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
            style(selectButton);
            center = new StackPane(godsScroll(godsView));
        } else if (chooseGodData.isPresent()) {
            action.setText("Choose a god to use");
            showAction(action, bottom);

            StringBinding text = Bindings.createStringBinding(() -> "Choose");
            BooleanBinding enabled = Bindings.createBooleanBinding(() -> true);

            List<GodBox> godComponentsList = new ArrayList<>();
            List<ImageView> godImageList = new ArrayList<>();
            for (GodInfo god : chooseGodData.get().getAvailableGods()) {

                ImageView godCard = getCard(god);
                godImageList.add(godCard);

                Button finalSelectButton = selectButton;
                EventHandler<ActionEvent> handler = (ignored) -> onChoose(god.getName());
                GodBox godBox = new GodBox(god, text, enabled, handler, getAssets().getFont());
                style(godBox.getButton());

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
        Pane pane = generatePresentation(width, height, center, bottom);

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

    private void onSelectFirst(String player) {
        getState().acceptSelectFirst(player);
    }

    private Pane generatePresentation(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height,
                                        StackPane center, StackPane bottom
    ) {
        GridPane pane = new GridPane();

        ImageView topImage = new ImageView(getAssets().getImage(GuiAssets.Images.LOBBY_TOP));
        topImage.setPreserveRatio(false);
        pane.add(topImage, 0, 0);

        ImageView background = new ImageView(getAssets().getImage(GuiAssets.Images.LOBBY_BACKGROUND));
        background.setPreserveRatio(false);
        background.fitWidthProperty().bind(width);

        center.setAlignment(Pos.CENTER);
        center.getChildren().add(0, background);
        pane.add(center, 0, 1);

        ImageView bottomImage = new ImageView(getAssets().getImage(GuiAssets.Images.LOBBY_BOTTOM));
        bottomImage.setPreserveRatio(false);

        background.fitHeightProperty().bind(Bindings.createDoubleBinding(() -> {
            double topHeight = topImage.getImage().getHeight();
            double bottomHeight = bottomImage.getImage().getHeight();
            return Math.max(GuiConstants.DEFAULT_SPACING, height.get() - (topHeight + bottomHeight));
        }, width, height));

        bottom.getChildren().add(0, bottomImage);
        pane.add(bottom, 0, 2);

        GridPane.setVgrow(center, Priority.ALWAYS);
        pane.setPadding(new Insets(0, 0, 0, 0));

        topImage.fitWidthProperty().bind(width);
        bottomImage.fitWidthProperty().bind(width);
        return pane;
    }

    private ImageView getCard(GodInfo god) {
        ImageView godCard = new ImageView(new Image(AbstractClientViewer.ASSETS_DIRECTORY + "image-card-" + god.getName().toLowerCase() + ".png"));
        godCard.setFitWidth(130);
        godCard.setPreserveRatio(true);
        style(godCard);

        return godCard;
    }

    private void showGodInfo(GodBox godBox, StackPane bottom) {
        removeChild(bottom);
        bottom.getChildren().add(godBox);
    }

    private void showDoneButton(Button done, Text action, StackPane bottom) {
        removeChild(bottom);

        action.setFont(getAssets().getFont());
        action.setTextAlignment(TextAlignment.CENTER);
        VBox vBox= new VBox(action, done);
        vBox.setAlignment(Pos.CENTER);
        bottom.getChildren().add(vBox);
    }

    private void removeChild(StackPane bottom) {
        try {
            bottom.getChildren().remove(1);
        } catch (IndexOutOfBoundsException e) {}
    }

    private void showAction(Text action, StackPane bottom) {
        action.setFont(getAssets().getFont());
        action.setTextAlignment(TextAlignment.CENTER);
        bottom.getChildren().add(action);
    }

    private ScrollPane godsScroll(FlowPane godsView) {
        godsView.setBackground(Background.EMPTY);
        ScrollPane godsScroll = new ScrollPane(godsView);
        godsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        godsScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        godsScroll.setBackground(Background.EMPTY);
        godsScroll.setFitToWidth(true);

        return godsScroll;
    }

    private void style(ImageView godCard) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(15.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0.25, 0.25, 0.25));
        godCard.setOnMouseEntered(event -> godCard.setEffect(dropShadow));
        godCard.setOnMouseExited(event -> godCard.setEffect(null));
    }

    /**
     * Style button
     * @param component The field
     */
    public void style(Button component) {
        Image img = getAssets().getImage(GuiAssets.Images.CREATE_ROOM_BUTTON);
        BackgroundSize bgSize = new BackgroundSize(1.0, 1.0, true, true, false , false);
        BackgroundImage backgroundImage = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bgSize);
        Background background = new Background(backgroundImage);
        component.setBackground(background);

        component.setFont(getAssets().getFont());
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(15.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0.2, 0.2, 0.2));
        component.setOnMouseEntered(event -> component.setEffect(dropShadow));
        component.setOnMouseExited(event -> component.setEffect(null));
    }

}
