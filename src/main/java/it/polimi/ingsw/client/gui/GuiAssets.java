package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.AbstractClientViewer;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class GuiAssets {

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

        ICON("image-icon.png"),
        BACKGROUND_MAIN("image-background-main.png"),
        BACKGROUND_MAIN_SUNSET("image-background-main-sunset.jpg"),
        LOGO_SUNSET("image-logo-sunset.png"),
        WIN_BACKGROUND("image-background-win.png"),
        WIN_MESSAGE("image-win.png"),
        BUTTON_MAIN("image-button-main.png"),
        INPUT_FIELD("image-input-field.png"),
        LOGO("image-logo.png"),
        LOBBY_BACKGROUND("image-lobby-background.png"),
        LOBBY_TOP("image-lobby-top.png"),
        LOBBY_BOTTOM("image-lobby-bottom.png"),
        GAMEOVER_BACKGROUND("image-background-gameover.png"),
        GAMEOVER_MESSAGE("image-gameover.png");

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

}
