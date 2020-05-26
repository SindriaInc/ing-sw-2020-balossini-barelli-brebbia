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
        BUTTON_MAIN("image-button-main.png"),
        INPUT_FIELD("image-input-field.png"),
        LOGO("image-logo.png");

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

    public GuiAssets() {
        for (Images image : Images.values()) {
            imageRegistry.put(image, new Image(AbstractClientViewer.ASSETS_DIRECTORY + image.getPath()));
        }

        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(AbstractClientViewer.ASSETS_DIRECTORY + FONT);
        font = Font.loadFont(stream, GuiConstants.DEFAULT_FONT_SIZE);
    }

    public Image getImage(Images image) {
        return imageRegistry.get(image);
    }

    public Font getFont() {
        return font;
    }

}
