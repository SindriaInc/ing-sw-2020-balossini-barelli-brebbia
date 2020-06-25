package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.AbstractClientViewer;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class GuiAssets {

    private static final String FONT = "diogenes.ttf";

    public enum Images {

        BACKGROUND_MAIN("image-background-main.png"),
        BACKGROUND_MAIN_SUNSET("image-background-main-sunset.jpg"),
        BUTTON_MAIN("image-button-main.png"),
        INPUT_FIELD("image-input-field.png"),
        LOGO("image-logo.png"),
        LOGO_SUNSET("image-logo-sunset.png"),
        WIN_BACKGROUND("image-background-win.png"),
        WIN_MESSAGE("image-win.png"),
        GAMEOVER_BACKGROUND("image-background-gameover.png"),
        GAMEOVER_MESSAGE("image-gameover.png");

        private final String path;

        Images(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }

    }

    private final Map<Images, Image> imageRegistry = new HashMap<>();

    private final Font font;

    private final Font inputButtonFont;

    private final Font endFont;

    public GuiAssets() {
        for (Images image : Images.values()) {
            imageRegistry.put(image, new Image(AbstractClientViewer.ASSETS_DIRECTORY + image.getPath()));
        }

        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(AbstractClientViewer.ASSETS_DIRECTORY + FONT);
        font = Font.loadFont(stream, GuiConstants.DEFAULT_FONT_SIZE);

        InputStream stream2 = this.getClass().getClassLoader().getResourceAsStream(AbstractClientViewer.ASSETS_DIRECTORY + FONT);
        endFont = Font.loadFont(stream2, GuiConstants.END_FONT_SIZE);

        InputStream stream3 = this.getClass().getClassLoader().getResourceAsStream(AbstractClientViewer.ASSETS_DIRECTORY + FONT);
        inputButtonFont = Font.loadFont(stream3, GuiConstants.BUTTON_FONT_SIZE);
    }

    public Image getImage(Images image) {
        return imageRegistry.get(image);
    }

    public Font getFont() {
        return font;
    }

    public Font getInputButtonFont() {
        return inputButtonFont;
    }

    public Font getEndFont() {
        return endFont;
    }

}
