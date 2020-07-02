package it.polimi.ingsw.client.gui.view.game;

import it.polimi.ingsw.client.clientstates.GameState;
import it.polimi.ingsw.client.data.GameData;
import it.polimi.ingsw.client.data.request.InteractData;
import it.polimi.ingsw.client.data.request.WorkersInteractData;
import it.polimi.ingsw.client.data.request.WorkersOtherInteractData;
import it.polimi.ingsw.client.gui.GuiAssets;
import it.polimi.ingsw.client.gui.view.component.BoardBox;
import it.polimi.ingsw.client.gui.view.presentation.BoardPresentation;
import it.polimi.ingsw.common.info.CellInfo;
import it.polimi.ingsw.common.info.Coordinates;
import it.polimi.ingsw.common.info.WorkerInfo;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Create the gui rendering of the board view
 */
public class GuiBoardView extends AbstractGameView {

    private Consumer<Coordinates> actionConsumer = (ignored) -> {};

    /**
     * Class constructor, set state and assets
     *
     * @param state The state
     * @param images The assets
     */
    public GuiBoardView(GameState state, GuiAssets images) {
        super(state, images);
    }

    /**
     * @see it.polimi.ingsw.client.gui.view.AbstractGuiView#generateView(ReadOnlyDoubleProperty, ReadOnlyDoubleProperty)
     */
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

        BoardBox boardBox = new BoardBox(getAssets(), map, coordinates -> actionConsumer.accept(coordinates));

        for (WorkerInfo worker : workers) {
            boardBox.setWorker(worker.getPosition(), worker);
        }

        Text infoText = new Text();
        List<Button> infoButtons = new ArrayList<>();

        spawn.ifPresent(spawnData -> infoButtons.addAll(generateSpawn(infoText, boardBox, spawnData)));
        move.ifPresent(moveData -> infoButtons.addAll(generateMove(infoText, boardBox, moveData)));
        buildBlock.ifPresent(buildBlockData -> infoButtons.addAll(generateBuildBlock(infoText, boardBox, buildBlockData)));
        buildDome.ifPresent(buildDomeData -> infoButtons.addAll(generateBuildDome(infoText, boardBox, buildDomeData)));
        force.ifPresent(forceData -> infoButtons.addAll(generateForce(infoText, boardBox, forceData)));

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

        if (data.isSpectating()) {
            infoText.setText("You're a spectator.\n\n" + infoText.getText());
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

        BoardPresentation presentation = new BoardPresentation(getAssets());
        return presentation.generatePresentation(width, height, boardBox, infoText, infoButtons);
    }

    /**
     * Generate a list of spawn buttons
     * @param text The text
     * @param boardBox The board representation
     * @param spawn The spawn data
     * @return The list of spawn buttons
     */
    private List<Button> generateSpawn(Text text, BoardBox boardBox, InteractData spawn) {
        String buttonText = "Spawn a worker";
        String textToSet = "Click on a cell to spawn a worker";
        Supplier<Consumer<Coordinates>> actionConsumerSupplier = () -> coordinates ->
                getState().acceptSpawn(coordinates.getX(), coordinates.getY());

        return List.of(generate(text, boardBox, spawn, buttonText, textToSet, actionConsumerSupplier));
    }

    /**
     * Generate a list of move buttons
     * @param text The text
     * @param boardBox The board representation
     * @param move The move data
     * @return The list of move buttons
     */
    private List<Button> generateMove(Text text, BoardBox boardBox, WorkersInteractData move) {
        List<Button> generated = new ArrayList<>();

        for (Map.Entry<Integer, InteractData> entry : move.getAvailableInteractions().entrySet()) {
            String buttonText = "Move the worker";

            if (move.getSingleWorkerInteraction().isEmpty()) {
                buttonText = "Move your " + getAssets().getReadableWorkerById(entry.getKey());
            }

            String textToSet = "Click on a cell to move the worker";
            Supplier<Consumer<Coordinates>> actionConsumerSupplier = () -> coordinates ->
                    getState().acceptMove(entry.getKey(), coordinates.getX(), coordinates.getY());

            generated.add(generate(text, boardBox, entry.getValue(), buttonText, textToSet, actionConsumerSupplier));
        }

        return generated;
    }

    /**
     * Generate a list of build block buttons
     * @param text The text
     * @param boardBox The board representation
     * @param buildBlock The build block data
     * @return The list of build block buttons
     */
    private List<Button> generateBuildBlock(Text text, BoardBox boardBox, WorkersInteractData buildBlock) {
        List<Button> generated = new ArrayList<>();

        for (Map.Entry<Integer, InteractData> entry : buildBlock.getAvailableInteractions().entrySet()) {
            String buttonText = "Build a block";

            if (buildBlock.getSingleWorkerInteraction().isEmpty()) {
                buttonText = "Build a block using the " + getAssets().getReadableWorkerById(entry.getKey());
            }

            String textToSet = "Click on a cell to build a block";
            Supplier<Consumer<Coordinates>> actionConsumerSupplier = () -> coordinates ->
                    getState().acceptBuildBlock(entry.getKey(), coordinates.getX(), coordinates.getY());

            generated.add(generate(text, boardBox, entry.getValue(), buttonText, textToSet, actionConsumerSupplier));
        }

        return generated;
    }

    /**
     * Generate a list of build dome buttons
     * @param text The text
     * @param boardBox The board representation
     * @param buildDome The build dome data
     * @return The list of build dome buttons
     */
    private List<Button> generateBuildDome(Text text, BoardBox boardBox, WorkersInteractData buildDome) {
        List<Button> generated = new ArrayList<>();

        for (Map.Entry<Integer, InteractData> entry : buildDome.getAvailableInteractions().entrySet()) {
            String buttonText = "Build a dome";

            if (buildDome.getSingleWorkerInteraction().isEmpty()) {
                buttonText = "Build a dome using the " + getAssets().getReadableWorkerById(entry.getKey());
            }

            String textToSet = "Click on a cell to build a dome";
            Supplier<Consumer<Coordinates>> actionConsumerSupplier = () -> coordinates ->
                    getState().acceptBuildDome(entry.getKey(), coordinates.getX(), coordinates.getY());

            generated.add(generate(text, boardBox, entry.getValue(), buttonText, textToSet, actionConsumerSupplier));
        }

        return generated;
    }

    /**
     * Generate a list of force buttons
     * @param text The text
     * @param boardBox The board representation
     * @param force The force data
     * @return The list of force buttons
     */
    private List<Button> generateForce(Text text, BoardBox boardBox, WorkersOtherInteractData force) {
        List<Button> generated = new ArrayList<>();

        for (Map.Entry<Integer, WorkersInteractData> workersEntry : force.getAvailableOtherInteractions().entrySet()) {
            int worker = workersEntry.getKey();
            WorkersInteractData workerForces = workersEntry.getValue();

            for (Map.Entry<Integer, InteractData> entry : workerForces.getAvailableInteractions().entrySet()) {
                int target = entry.getKey();
                String buttonText = "Force the " + getAssets().getReadableWorkerById(target);

                if (workerForces.getSingleWorkerInteraction().isEmpty()) {
                    buttonText = "Force the " + getAssets().getReadableWorkerById(target) + " using the " + getAssets().getReadableWorkerById(target);
                }

                String textToSet = "Click on a cell to force the " + getAssets().getReadableWorkerById(target) + " there";
                Supplier<Consumer<Coordinates>> actionConsumerSupplier = () -> coordinates ->
                        getState().acceptForce(worker, target, coordinates.getX(), coordinates.getY());

                generated.add(generate(text, boardBox, entry.getValue(), buttonText, textToSet, actionConsumerSupplier));
            }
        }

        return generated;
    }

    /**
     * Generate a button for a possible interaction
     * @param text The text
     * @param boardBox The board box
     * @param data The data of the interaction
     * @param buttonText The text for the button
     * @param textToSet The text to set
     * @param actionConsumerSupplier The action consumer supplier
     * @return
     */
    private Button generate(Text text, BoardBox boardBox, InteractData data, String buttonText,
                            String textToSet, Supplier<Consumer<Coordinates>> actionConsumerSupplier) {
        Button button = new Button();
        button.setText(buttonText);
        button.setOnAction((event) -> {
            updateButtons(boardBox, data.getAvailableCoordinates());
            actionConsumer = actionConsumerSupplier.get();
            text.setText(textToSet);
        });
        return button;
    }

    /**
     * Update the button list
     * @param boardBox The board box
     * @param enabledCoordinates The enabled coordinates
     */
    private void updateButtons(BoardBox boardBox, List<Coordinates> enabledCoordinates) {
        for (Coordinates position : boardBox.getPositions()) {
            boardBox.setEnabled(position, enabledCoordinates.contains(position));
        }
    }

}
