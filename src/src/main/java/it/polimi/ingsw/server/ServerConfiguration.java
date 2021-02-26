package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.abilities.AbilitiesDecorator;

import javax.naming.ConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The server configuration, containing the parameters used to determine the port binding, gods and the log path
 */
public class ServerConfiguration {

    /**
     * A god card effect, specifying which decorators will apply to the players
     */
    private static class EffectConfiguration {

        /**
         * The name of the effect, must match one of the classes in <code>it.polimi.ingsw.model.abilities.decorators</code>
         */
        private final String name;

        /**
         * Whether or not the effect is applied to opponents instead of the player that owns the god card
         */
        private final Boolean opponents;

        /**
         * Class constructor
         * Called by the configuration deserializer
         *
         * @param name The effect name
         * @param opponents The opponent status
         */
        public EffectConfiguration(String name, Boolean opponents) {
            this.name = name;
            this.opponents = opponents;
        }

        /**
         * Whether or not this section of the config has all the parameters
         * @return true if the data is valid
         */
        public boolean isValid() {
            return name != null && opponents != null;
        }

    }

    /**
     * A god card, which contains informations on the god including its effects
     */
    private static class GodConfiguration {

        /**
         * The god id
         */
        private final Integer id;

        /**
         * The god description
         */
        private final String description;

        /**
         * The god name
         */
        private final String name;

        /**
         * The god title
         */
        private final String title;

        /**
         * The god type
         */
        private final String type;

        /**
         * The list of effects
         */
        private final List<EffectConfiguration> effects;

        /**
         * Class constructor
         * Called by the configuration deserializer
         *
         * @param id The god id
         * @param description The god description
         * @param name The god name
         * @param title The god title
         * @param type The god type
         * @param effects The god effects
         */
        public GodConfiguration(Integer id, String description, String name, String title, String type, List<EffectConfiguration> effects) {
            this.id = id;
            this.description = description;
            this.name = name;
            this.title = title;
            this.type = type;
            this.effects = effects;
        }

        /**
         * Whether or not this section of the config has all the parameters
         * @return true if the data is valid
         */
        public boolean isValid() {
            return id != null && description != null && name != null && title != null && type != null && effects != null;
        }

    }

    private static final String EFFECTS_PACKAGE = "it.polimi.ingsw.model.abilities.decorators.";

    private static final int DEFAULT_PORT = 25565;

    private static final String DEFAULT_LOG_PATH = "./latest.log";

    private static final String RESOURCE_DECK_FILE = "gods.json";

    /**
     * The server port
     * If null, the default server port will be used instead
     */
    private final Integer port;

    /**
     * The path where the log file will be saved
     * If null, the default log path will be used instead
     */
    private final String logPath;

    /**
     * The path to the file from which the deck should be loaded from
     * If null, the deck will be loaded from the application jar
     */
    private final String deckPath;

    /**
     * Reads the default server configuration
     * @return The configuration
     */
    public static ServerConfiguration readDefault() {
        return new ServerConfiguration(null, null, null);
    }

    /**
     * Reads the server configuration from a file
     * @param configPath The file path
     * @return The configuration
     * @throws IOException if there's an error while reading or parsing the file
     */
    public static ServerConfiguration readFromFile(String configPath) throws IOException {
        JsonReader jsonReader = new JsonReader(new FileReader(configPath));
        return new Gson().fromJson(jsonReader, ServerConfiguration.class);
    }

    /**
     * Class constructor
     * Called by the configuration deserializer
     *
     * @param port The server port
     * @param logPath The log path
     * @param deckPath The deck configuration path
     */
    private ServerConfiguration(Integer port, String logPath, String deckPath) {
        this.port = port;
        this.logPath = logPath;
        this.deckPath = deckPath;
    }

    /**
     * Creates a new <code>ServerConfiguration</code> with the specified port
     * @param port The port
     * @return The new configuration
     */
    public ServerConfiguration withPort(int port) {
        return new ServerConfiguration(port, logPath, deckPath);
    }

    /**
     * Creates a new <code>ServerConfiguration</code> with the specified log path
     * @param logPath The log path
     * @return The new configuration
     */
    public ServerConfiguration withLogPath(String logPath) {
        return new ServerConfiguration(port, logPath, deckPath);
    }

    /**
     * Creates a new <code>ServerConfiguration</code> with the specified deck path
     * @param deckPath The deck path
     * @return The new configuration
     */
    public ServerConfiguration withDeckPath(String deckPath) {
        return new ServerConfiguration(port, logPath, deckPath);
    }

    /**
     * The server port, or <code>DEFAULT_PORT</code> if not configured
     * @return The port
     */
    public Integer getPort() {
        if (port == null) {
            return DEFAULT_PORT;
        }

        return port;
    }

    /**
     * The server log path, or <code>DEFAULT_LOG_PATH</code> if not configured
     * @return The log path
     */
    public String getLogPath() {
        if (logPath == null) {
            return DEFAULT_LOG_PATH;
        }

        return logPath;
    }

    /**
     * Loads the deck configuration from the jar of the deck path (if specified)
     *
     * @return The loaded deck
     * @throws ConfigurationException if invalid configuration parameters are found
     * @throws IOException if the deck file can't be loaded
     */
    @SuppressWarnings("unchecked")
    public Deck loadDeck() throws ConfigurationException, IOException {
        Reader reader;

        if (deckPath == null) {
            // Read from the jar
            InputStream stream = this.getClass().getClassLoader().getResourceAsStream(RESOURCE_DECK_FILE);

            if (stream == null) {
                throw new IOException("Unable to find the deck file in the jar");
            }

            reader = new InputStreamReader(stream);
        } else {
            // Read an external file
            reader = new FileReader(deckPath);
        }

        JsonReader jsonReader = new JsonReader(reader);
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
