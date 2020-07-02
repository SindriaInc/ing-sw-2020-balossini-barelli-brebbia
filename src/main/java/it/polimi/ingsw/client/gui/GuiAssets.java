package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.AbstractClientViewer;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class GuiAssets {

    private enum WorkerColors {

        BLUE,
        ORANGE,
        GRAY,
        PINK;

        public String getReadableName() {
            return name().toLowerCase();
        }

    }

    private enum WorkerTypes {

        F("female"),
        M("male");

        private final String readableName;

        WorkerTypes(String readableName) {
            this.readableName = readableName;
        }

        public String getReadableName() {
            return readableName;
        }

    }

    private static final String FONT = "diogenes.ttf";

    public enum Styles {

        DEFAULT("style.css");

        private final String path;

        Styles(String path) {
            this.path = path;
        }

        private String getPath() {
            return path;
        }

    }

    public enum Images {

        // Main assets
        ICON("image-icon.png"),
        BACKGROUND_MAIN("image-background-main.png"),
        BACKGROUND_MAIN_SUNSET("image-background-main-sunset.jpg"),
        LOGO_SUNSET("image-logo-sunset.png"),
        BUTTON_MAIN("image-button-main.png"),
        INPUT_FIELD("image-input-field.png"),
        LOGO("image-logo.png"),

        // Lobby assets
        LOBBY_BACKGROUND("image-lobby-background.png"),
        LOBBY_TOP("image-lobby-top.png"),
        LOBBY_BOTTOM("image-lobby-bottom.png"),
        CREATE_ROOM_BUTTON("image-create-room-button.png"),

        // Game assets
        WORKER_BLUE_M("image-worker-blue-m.png"),
        WORKER_BLUE_F("image-worker-blue-f.png"),
        WORKER_ORANGE_M("image-worker-orange-m.png"),
        WORKER_ORANGE_F("image-worker-orange-f.png"),
        WORKER_GRAY_M("image-worker-gray-m.png"),
        WORKER_GRAY_F("image-worker-gray-f.png"),
        WORKER_PINK_M("image-worker-pink-m.png"),
        WORKER_PINK_F("image-worker-pink-f.png"),
        BLOCK1("image-block1.png"),
        BLOCK2("image-block1-2.png"),
        BLOCK3("image-block1-2-3.png"),
        DOME("image-block1-2-3-D.png"),
        GAME_BOARD("image-game-board.png"),
        GAME_SIDE("image-game-side.png"),
        GAME_BACKGROUND("image-game-background.png"),

        // End assets
        GAMEOVER_BACKGROUND("image-background-gameover.png"),
        GAMEOVER_MESSAGE("image-gameover.png"),
        WIN_BACKGROUND("image-background-win.png"),
        WIN_MESSAGE("image-win.png");

        private final String path;

        Images(String path) {
            this.path = path;
        }

        private String getPath() {
            return path;
        }

    }

    private final Map<Images, Image> imageRegistry = new HashMap<>();

    private final Font font;

    private final Font titleFont;

    private final Font inputButtonFont;

    private final Font endFont;

    public GuiAssets() {
        for (Images image : Images.values()) {
            imageRegistry.put(image, new Image(AbstractClientViewer.ASSETS_DIRECTORY + image.getPath()));
        }

        InputStream fontStream = this.getClass().getClassLoader().getResourceAsStream(AbstractClientViewer.ASSETS_DIRECTORY + FONT);
        font = Font.loadFont(fontStream, GuiConstants.DEFAULT_FONT_SIZE);

        InputStream titleFontStream = this.getClass().getClassLoader().getResourceAsStream(AbstractClientViewer.ASSETS_DIRECTORY + FONT);
        titleFont = Font.loadFont(titleFontStream, GuiConstants.TITLE_FONT_SIZE);

        InputStream endFontStream = this.getClass().getClassLoader().getResourceAsStream(AbstractClientViewer.ASSETS_DIRECTORY + FONT);
        endFont = Font.loadFont(endFontStream, GuiConstants.END_FONT_SIZE);

        InputStream inputButtonFontStream = this.getClass().getClassLoader().getResourceAsStream(AbstractClientViewer.ASSETS_DIRECTORY + FONT);
        inputButtonFont = Font.loadFont(inputButtonFontStream, GuiConstants.BUTTON_FONT_SIZE);
    }

    public Image getImage(Images image) {
        return imageRegistry.get(image);
    }

    public String getStyle(Styles style) {
        return getClass()
                .getResource(
                        "/" + AbstractClientViewer.ASSETS_DIRECTORY + style.getPath())
                .toExternalForm();
    }

    public Font getFont() {
        return font;
    }

    public Font getTitleFont() {
        return titleFont;
    }

    public Font getInputButtonFont() {
        return inputButtonFont;
    }

    public Font getEndFont() {
        return endFont;
    }

    public String getReadableWorkerById(int workerId) {
        return getWorkerColorById(workerId).getReadableName() + " " + getWorkerTypeById(workerId).getReadableName();
    }

    public Image getWorkerById(int workerId) {
        WorkerColors color = getWorkerColorById(workerId);
        WorkerTypes type = getWorkerTypeById(workerId);

        return getImage(Images.valueOf("WORKER_" + color.name() + "_" + type.name()));
    }

    private WorkerColors getWorkerColorById(int workerId) {
        return WorkerColors.values()[(workerId / WorkerTypes.values().length) % WorkerColors.values().length];
    }

    private WorkerTypes getWorkerTypeById(int workerId) {
        return WorkerTypes.values()[workerId % WorkerTypes.values().length];
    }

}
