package it.polimi.ingsw.client.gui.view.game;

import it.polimi.ingsw.client.clientstates.GameState;
import it.polimi.ingsw.client.data.GameData;
import it.polimi.ingsw.client.data.request.InteractData;
import it.polimi.ingsw.client.data.request.WorkersInteractData;
import it.polimi.ingsw.client.data.request.WorkersOtherInteractData;
import it.polimi.ingsw.client.gui.GuiConstants;
import it.polimi.ingsw.client.gui.GuiAssets;
import it.polimi.ingsw.common.info.CellInfo;
import it.polimi.ingsw.common.info.Coordinates;
import it.polimi.ingsw.common.info.WorkerInfo;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class GuiBoardView extends AbstractGameView {

    private Consumer<Coordinates> actionConsumer = (ignored) -> {};

    public GuiBoardView(GameState state, GuiAssets images) {
        super(state, images);
    }

    @Override
    public Parent generateView(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height) {
        GameData data = getState().getData();

        Optional<String> turn = data.getTurnPlayer();
        CellInfo[][] map = data.getMap();
        List<WorkerInfo> workers = data.getWorkers();

        Optional<InteractData> spawn = data.getSpawnData();
        Optional<WorkersInteractData> move = data.getMoveData();
        Optional<WorkersInteractData> buildBlock = data.getBuildBlockData();
        Optional<WorkersInteractData> buildDome = data.getBuildDomeData();
        Optional<WorkersOtherInteractData> force = data.getForceData();
        boolean canBeEnded = data.getCanBeEnded().isPresent() && data.getCanBeEnded().get();

        Map<Coordinates, Button> buttons = new HashMap<>();

        GridPane boardPane = new GridPane();

        for (int x = 0; x < map.length; x++) {
            CellInfo[] column = map[x];

            for (int y = 0; y < column.length; y++) {
                CellInfo cell = column[y];
                Coordinates coordinates = new Coordinates(x, y);

                StringBuilder text = new StringBuilder();
                if (cell.isDoomed()) {
                    text.append("(");
                }
                text.append(column[y].getLevel());
                if (cell.isDoomed()) {
                    text.append(")");
                }

                Button button = new Button();
                button.setText(text.toString());
                button.setOnAction((event) -> actionConsumer.accept(coordinates));
                button.setDisable(true);
                button.setMinWidth(GuiConstants.CELL_MIN_SIZE);
                button.setMinHeight(GuiConstants.CELL_MIN_SIZE);

                buttons.put(new Coordinates(x, y), button);
                boardPane.add(button, x, y);

                GridPane.setFillWidth(button, true);
                GridPane.setFillHeight(button, true);
            }
        }

        for (WorkerInfo worker : workers) {
            Button button = buttons.get(worker.getPosition());
            button.setText(button.getText() + "\n" + "W: " + worker.getId());
        }

        GridPane infoPane = new GridPane();
        Text infoText = new Text();
        List<Button> infoButtons = new ArrayList<>();

        spawn.ifPresent(spawnData -> infoButtons.addAll(generateSpawn(infoText, buttons, spawnData)));
        move.ifPresent(moveData -> infoButtons.addAll(generateMove(infoText, buttons, moveData)));
        buildBlock.ifPresent(buildBlockData -> infoButtons.addAll(generateBuildBlock(infoText, buttons, buildBlockData)));
        buildDome.ifPresent(buildDomeData -> infoButtons.addAll(generateBuildDome(infoText, buttons, buildDomeData)));
        force.ifPresent(forceData -> infoButtons.addAll(generateForce(infoText, buttons, forceData)));

        if (turn.isEmpty()) {
            infoText.setText("Waiting for other players...");
        } else if (!turn.get().equals(data.getName())) {
            infoText.setText("Currently playing: " + turn.get());
        } else if (infoButtons.size() == 0) {
            infoText.setText("Waiting for the server...");
        } else if (infoButtons.size() == 1 && !canBeEnded) {
            infoButtons.get(0).fire();
            infoButtons.clear();
        } else {
            infoText.setText("Select an action...");
        }

        if (canBeEnded) {
            Button endButton = new Button();
            endButton.setText("End your turn");
            endButton.setOnAction((event) -> getState().acceptEnd());

            if (infoButtons.size() == 0) {
                infoText.setText("Click on the button below to end your turn");
            }

            infoButtons.add(endButton);
        }

        boardPane.setGridLinesVisible(true);
        boardPane.setAlignment(Pos.CENTER);

        infoPane.add(infoText, 0, 0);
        int index = 1;
        for (Button button : infoButtons) {
            infoPane.add(button, 0, index++);
        }

        infoPane.setVgap(GuiConstants.DEFAULT_SPACING);
        infoPane.setMinWidth(GuiConstants.INFO_MIN_SIZE);

        GridPane pane = new GridPane();
        pane.setHgap(GuiConstants.DEFAULT_SPACING);
        pane.setAlignment(Pos.CENTER);
        pane.add(boardPane, 0, 0);
        pane.add(infoPane, 1, 0);
        return pane;
    }

    private List<Button> generateSpawn(Text text, Map<Coordinates, Button> buttons, InteractData spawn) {
        String buttonText = "Spawn a worker";
        String textToSet = "Click on a cell to spawn a worker";
        Supplier<Consumer<Coordinates>> actionConsumerSupplier = () -> coordinates ->
                getState().acceptSpawn(coordinates.getX(), coordinates.getY());

        return List.of(generate(text, buttons, spawn, buttonText, textToSet, actionConsumerSupplier));
    }

    private List<Button> generateMove(Text text, Map<Coordinates, Button> buttons, WorkersInteractData move) {
        List<Button> generated = new ArrayList<>();

        for (Map.Entry<Integer, InteractData> entry : move.getAvailableInteractions().entrySet()) {
            String buttonText = "Move the worker";

            if (move.getSingleWorkerInteraction().isEmpty()) {
                buttonText = "Move your worker #" + entry.getKey();
            }

            String textToSet = "Click on a cell to move the worker";
            Supplier<Consumer<Coordinates>> actionConsumerSupplier = () -> coordinates ->
                    getState().acceptMove(entry.getKey(), coordinates.getX(), coordinates.getY());

            generated.add(generate(text, buttons, entry.getValue(), buttonText, textToSet, actionConsumerSupplier));
        }

        return generated;
    }

    private List<Button> generateBuildBlock(Text text, Map<Coordinates, Button> buttons, WorkersInteractData buildBlock) {
        List<Button> generated = new ArrayList<>();

        for (Map.Entry<Integer, InteractData> entry : buildBlock.getAvailableInteractions().entrySet()) {
            String buttonText = "Build a block";

            if (buildBlock.getSingleWorkerInteraction().isEmpty()) {
                buttonText = "Build a block using the worker #" + entry.getKey();
            }

            String textToSet = "Click on a cell to build a block";
            Supplier<Consumer<Coordinates>> actionConsumerSupplier = () -> coordinates ->
                    getState().acceptBuildBlock(entry.getKey(), coordinates.getX(), coordinates.getY());

            generated.add(generate(text, buttons, entry.getValue(), buttonText, textToSet, actionConsumerSupplier));
        }

        return generated;
    }

    private List<Button> generateBuildDome(Text text, Map<Coordinates, Button> buttons, WorkersInteractData buildDome) {
        List<Button> generated = new ArrayList<>();

        for (Map.Entry<Integer, InteractData> entry : buildDome.getAvailableInteractions().entrySet()) {
            String buttonText = "Build a dome";

            if (buildDome.getSingleWorkerInteraction().isEmpty()) {
                buttonText = "Build a dome using the worker #" + entry.getKey();
            }

            String textToSet = "Click on a cell to build a dome";
            Supplier<Consumer<Coordinates>> actionConsumerSupplier = () -> coordinates ->
                    getState().acceptBuildDome(entry.getKey(), coordinates.getX(), coordinates.getY());

            generated.add(generate(text, buttons, entry.getValue(), buttonText, textToSet, actionConsumerSupplier));
        }

        return generated;
    }

    private List<Button> generateForce(Text text, Map<Coordinates, Button> buttons, WorkersOtherInteractData force) {
        List<Button> generated = new ArrayList<>();

        for (Map.Entry<Integer, WorkersInteractData> workersEntry : force.getAvailableOtherInteractions().entrySet()) {
            int worker = workersEntry.getKey();
            WorkersInteractData workerForces = workersEntry.getValue();

            for (Map.Entry<Integer, InteractData> entry : workerForces.getAvailableInteractions().entrySet()) {
                int target = entry.getKey();
                String buttonText = "Force the worker #" + target;

                if (workerForces.getSingleWorkerInteraction().isEmpty()) {
                    buttonText = "Force #" + target + " using the worker #" + worker;
                }

                String textToSet = "Click on a cell to force #" + target + " there";
                Supplier<Consumer<Coordinates>> actionConsumerSupplier = () -> coordinates ->
                        getState().acceptForce(worker, target, coordinates.getX(), coordinates.getY());

                generated.add(generate(text, buttons, entry.getValue(), buttonText, textToSet, actionConsumerSupplier));
            }
        }

        return generated;
    }

    private Button generate(Text text, Map<Coordinates, Button> buttons, InteractData data, String buttonText,
                            String textToSet, Supplier<Consumer<Coordinates>> actionConsumerSupplier) {
        Button button = new Button();
        button.setText(buttonText);
        button.setOnAction((event) -> {
            updateButtons(buttons, data.getAvailableCoordinates());
            actionConsumer = actionConsumerSupplier.get();
            text.setText(textToSet);
        });
        return button;
    }

    private void updateButtons(Map<Coordinates, Button> buttons, List<Coordinates> enabledCoordinates) {
        for (Map.Entry<Coordinates, Button> entry : buttons.entrySet()) {
            Coordinates coordinates = entry.getKey();
            Button button = entry.getValue();

            button.setDisable(!enabledCoordinates.contains(coordinates));
        }
    }

}
