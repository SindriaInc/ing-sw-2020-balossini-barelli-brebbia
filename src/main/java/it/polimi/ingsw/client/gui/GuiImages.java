package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.AbstractClientViewer;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class GuiImages {

    public enum Images {

        BACKGROUND_MAIN("image-background-main.png"),
        BUTTON_MAIN("image-button-main.png"),
        INPUT_FIELD("image-input-field.png"),
        LOGO("image-input-field.png");

        private final String path;

        Images(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }

    }

    public static final String IMAGE_BACKGROUND_MAIN = "image-background-main.png";
    public static final String IMAGE_BUTTON_MAIN = "image-button-main.png";
    public static final String IMAGE_INPUT_FIELD = "image-input-field.png";
    public static final String IMAGE_LOGO = "image-input-field.png";

    private static final String[] IMAGES = {
            IMAGE_BACKGROUND_MAIN,
            IMAGE_BUTTON_MAIN,
            IMAGE_INPUT_FIELD,
            IMAGE_LOGO
    };

    private Map<Images, Image> imageRegistry = new HashMap<>();

    public GuiImages() {
        for (Images image : Images.values()) {
            imageRegistry.put(image, new Image(AbstractClientViewer.ASSETS_DIRECTORY + image.getPath()));
        }
    }

    public Image getImage(Images image) {
        return imageRegistry.get(image);
    }

}
