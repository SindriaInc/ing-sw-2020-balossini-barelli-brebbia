package it.polimi.ingsw.client.gui.view.component;

import it.polimi.ingsw.client.gui.GuiAssets;
import it.polimi.ingsw.client.gui.GuiConstants;
import it.polimi.ingsw.common.info.CellInfo;
import it.polimi.ingsw.common.info.Coordinates;
import it.polimi.ingsw.common.info.WorkerInfo;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class BoardBox extends GridPane {

    private static final double RELATIVE_WORKER_SIZE = 0.8D;
    private static final double RELATIVE_BACKGROUND_SIZE = 0.8D;
    private static final double ENABLED_CELL_COLOR_OPACITY = 0.75D;
    private static final int BACKGROUND_INDEX = 2;
    private static final int WORKER_INDEX = 2;

    private static class BoardCell {

        /**
         * The pane on which the cell is drawn
         *
         * The following children will always be present (or an empty pane if not needed)
         * Z-0: Block
         * Z-1: Dome
         * Z-2: Selected background (BACKGROUND_INDEX)
         * Z-3: Worker (WORKER_INDEX)
         */
        private final StackPane pane;
        private boolean enabled;
        private WorkerInfo workerInfo;

        public BoardCell(StackPane pane) {
            this.pane = pane;
        }

        public StackPane getPane() {
            return pane;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public void setWorkerInfo(WorkerInfo workerInfo) {
            this.workerInfo = workerInfo;
        }
    }

    private final Map<Coordinates, BoardCell> cells = new HashMap<>();

    private final GuiAssets assets;

    public BoardBox(GuiAssets assets, CellInfo[][] map, Consumer<Coordinates> actionConsumer) {
        this.assets = assets;

        for (int x = 0; x < map.length; x++) {
            CellInfo[] column = map[x];

            for (int y = column.length - 1; y >= 0; y--) {
                CellInfo cell = column[y];
                Coordinates coordinates = new Coordinates(x, y);

                StackPane pane = new StackPane();
                GuiAssets.Images background = null;
                if (cell.getLevel() == 1) {
                    background = GuiAssets.Images.BLOCK1;
                } else if (cell.getLevel() == 2) {
                    background = GuiAssets.Images.BLOCK2;
                } else if (cell.getLevel() == 3) {
                    background = GuiAssets.Images.BLOCK3;
                }

                if (background != null) {
                    ImageView backgroundView = new ImageView(assets.getImage(background));
                    fitInCell(backgroundView);
                    pane.getChildren().add(backgroundView);
                } else {
                    pane.getChildren().add(new Pane());
                }

                if (cell.isDoomed()) {
                    ImageView domeView = new ImageView(assets.getImage(GuiAssets.Images.DOME));
                    fitInCell(domeView);
                    pane.getChildren().add(domeView);
                } else {
                    pane.getChildren().add(new Pane());
                }

                pane.setMinWidth(GuiConstants.CELL_MIN_SIZE);
                pane.setMinHeight(GuiConstants.CELL_MIN_SIZE);

                // Add an empty background
                pane.getChildren().add(BACKGROUND_INDEX, new Pane());

                // Add an empty worker
                pane.getChildren().add(WORKER_INDEX, new Pane());

                cells.put(coordinates, new BoardCell(pane));
                add(pane, x, column.length - (y + 1));
            }
        }

        setMinWidth(GuiConstants.CELL_MIN_SIZE * getColumnCount());
        setMinHeight(GuiConstants.CELL_MIN_SIZE * getRowCount());

        setOnMouseClicked(event -> {
            Node node = event.getPickResult().getIntersectedNode();

            // Find the clicked pane
            if (!node.getParent().equals(this)) {
                node = node.getParent();
            }

            Integer x = GridPane.getColumnIndex(node);
            Integer y = GridPane.getRowIndex(node);

            if (x == null || y == null) {
                return;
            }

            // Convert to game coordinates
            y = getRowCount() - (y + 1);

            Coordinates position = new Coordinates(x, y);

            if (!cells.get(position).isEnabled()) {
                return;
            }

            actionConsumer.accept(position);
        });
    }

    public List<Coordinates> getPositions() {
        return List.copyOf(cells.keySet());
    }

    public void setEnabled(Coordinates position, boolean enabled) {
        cells.get(position).setEnabled(enabled);

        StackPane cellPane = cells.get(position).getPane();
        cellPane.getChildren().remove(BACKGROUND_INDEX);

        if (!enabled) {
            cellPane.getChildren().add(BACKGROUND_INDEX, new Pane());
            return;
        }

        Pane pane = new Pane();
        pane.setMinWidth(GuiConstants.CELL_MIN_SIZE);
        pane.setMinHeight(GuiConstants.CELL_MIN_SIZE);
        pane.setOpacity(ENABLED_CELL_COLOR_OPACITY);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(GuiConstants.DEFAULT_RADII), Insets.EMPTY)));
        pane.setScaleX(RELATIVE_BACKGROUND_SIZE);
        pane.setScaleY(RELATIVE_BACKGROUND_SIZE);

        cellPane.getChildren().add(BACKGROUND_INDEX, pane);
    }

    public void setWorker(Coordinates position, WorkerInfo worker) {
        cells.get(position).setWorkerInfo(worker);

        StackPane cellPane = cells.get(position).getPane();
        cellPane.getChildren().remove(WORKER_INDEX);

        ImageView workerImage = new ImageView(assets.getWorkerById(worker.getId()));
        fitInCell(workerImage);
        workerImage.setScaleX(RELATIVE_WORKER_SIZE);
        workerImage.setScaleY(RELATIVE_WORKER_SIZE);

        cellPane.getChildren().add(workerImage);
    }

    private void fitInCell(ImageView imageView) {
        imageView.setFitWidth(GuiConstants.CELL_MIN_SIZE);
        imageView.setFitHeight(GuiConstants.CELL_MIN_SIZE);
    }

}
