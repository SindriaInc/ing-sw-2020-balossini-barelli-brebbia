package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.abilities.AbilitiesDecorator;

import javax.naming.ConfigurationException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerConfiguration {

    private static class EffectConfiguration {

        private final String name;
        private final Boolean opponents;

        public EffectConfiguration(String name, Boolean opponents) {
            this.name = name;
            this.opponents = opponents;
        }

        public boolean isValid() {
            return name != null && opponents != null;
        }

    }

    private static class GodConfiguration {

        private final Integer id;
        private final String description;
        private final String name;
        private final String title;
        private final String type;
        private final List<EffectConfiguration> effects;

        public GodConfiguration(Integer id, String description, String name, String title, String type, List<EffectConfiguration> effects) {
            this.id = id;
            this.description = description;
            this.name = name;
            this.title = title;
            this.type = type;
            this.effects = effects;
        }

        public boolean isValid() {
            return id != null && description != null && name != null && title != null && type != null && effects != null;
        }

    }

    private static final String EFFECTS_PACKAGE = "it.polimi.ingsw.model.abilities.decorators.";

    private static final int DEFAULT_PORT = 25565;

    private static final String DEFAULT_LOG_PATH = "../log.txt";

    private static final String DEFAULT_DECK_FILE = "gods.json";

    private final Integer port;

    private final String logPath;

    private final String deckPath;

    public static ServerConfiguration readDefault() {
        return new ServerConfiguration(null, null, null);
    }

    public static ServerConfiguration readFromFile(String configPath) throws IOException {
        JsonReader jsonReader = new JsonReader(new FileReader(configPath));
        return new Gson().fromJson(jsonReader, ServerConfiguration.class);
    }

    private ServerConfiguration(Integer port, String logPath, String deckPath) {
        this.port = port;
        this.logPath = logPath;
        this.deckPath = deckPath;
    }

    public ServerConfiguration withPort(int port) {
        return new ServerConfiguration(port, logPath, deckPath);
    }

    public ServerConfiguration withLogPath(String logPath) {
        return new ServerConfiguration(port, logPath, deckPath);
    }

    public ServerConfiguration withDeckPath(String deckPath) {
        return new ServerConfiguration(port, logPath, deckPath);
    }

    public Integer getPort() {
        if (port == null) {
            return DEFAULT_PORT;
        }

        return port;
    }

    public String getLogPath() {
        if (logPath == null) {
            return DEFAULT_LOG_PATH;
        }

        return logPath;
    }

    public String getDeckPath() {
        if (deckPath == null) {
            URL deckUrl = this.getClass().getClassLoader().getResource(DEFAULT_DECK_FILE);

            if (deckUrl == null) {
                return null;
            }

            return deckUrl.getPath();
        }

        return deckPath;
    }

    @SuppressWarnings("unchecked")
    public Deck loadDeck() throws ConfigurationException, IOException {
        String deckPath = getDeckPath();

        if (deckPath == null) {
            throw new ConfigurationException("Unable to find deck file");
        }

        JsonReader jsonReader = new JsonReader(new FileReader(deckPath));
        GodConfiguration[] godConfigurations = new Gson().fromJson(jsonReader, GodConfiguration[].class);

        List<God> gods = new ArrayList<>();
        for (GodConfiguration godConfiguration : godConfigurations) {
            if (!godConfiguration.isValid()) {
                throw new ConfigurationException("Invalid god: " + godConfiguration.name);
            }

            if (gods.stream().anyMatch(god -> god.getId() == godConfiguration.id)) {
                throw new ConfigurationException("Duplicate id found: " + godConfiguration.id);
            }

            Map<Class<? extends AbilitiesDecorator>, Boolean> effects = new HashMap<>();

            for (EffectConfiguration effectConfiguration : godConfiguration.effects) {
                if (!effectConfiguration.isValid()) {
                    throw new ConfigurationException("Invalid effects for god: " + godConfiguration.name);
                }

                Class<? extends AbilitiesDecorator> effect;
                try {
                    effect = (Class<? extends AbilitiesDecorator>) Class.forName(EFFECTS_PACKAGE + effectConfiguration.name);
                } catch (ClassNotFoundException exception) {
                    throw new ConfigurationException("Invalid effect for god " + godConfiguration.name + ": " + effectConfiguration.name);
                }

                effects.put(effect, effectConfiguration.opponents);
            }

            God god = new God(godConfiguration.name, godConfiguration.id, godConfiguration.title, godConfiguration.description, godConfiguration.type, effects);
            gods.add(god);
        }

        return new Deck(gods);
    }

}
