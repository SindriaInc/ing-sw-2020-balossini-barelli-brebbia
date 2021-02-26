package it.polimi.ingsw.common.serializer;

import com.google.gson.*;
import com.google.gson.annotations.Expose;
import it.polimi.ingsw.common.event.AbstractEvent;

/**
 * Serializes and deserializes extensions of AbstractEvent, transforming them from/to JSON
 *
 * Events must be in the EVENTS_PACKAGE
 */
public class GsonEventSerializer implements ISerializer<AbstractEvent> {

    private static final String EVENTS_PACKAGE = "it.polimi.ingsw.common.event.";
    private static final String PROPERTY_ATTRIBUTES = "attributes";
    private static final String PROPERTY_EVENT = "event";

    private static final String NULL_EVENT = "Null event name";
    private static final String INVALID_EVENT = "Invalid event name";
    private static final String NO_ATTRIBUTES = "No attributes found";

    private final Gson gson;

    /**
     * Class constructor
     */
    public GsonEventSerializer() {
        GsonBuilder builder = new GsonBuilder();

        // Exclude from serialization fields with @Expose(false)
        builder.addSerializationExclusionStrategy(new ExclusionStrategy() {

            @Override
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                Expose expose = fieldAttributes.getAnnotation(Expose.class);

                if (expose == null) {
                    return false;
                }

                return !expose.serialize();
            }

            @Override
            public boolean shouldSkipClass(Class<?> aClass) {
                return false;
            }

        });

        gson = builder.create();
    }

    /**
     * Serializes an event, converting it to a JSON string
     *
     * @param object The event
     * @return The serialized event
     */
    @Override
    public String serialize(AbstractEvent object) {
        JsonElement attributes = gson.toJsonTree(object);
        JsonObject json = new JsonObject();
        json.add(PROPERTY_ATTRIBUTES, attributes);
        json.addProperty(PROPERTY_EVENT, object.getClass().getSimpleName());
        return json.toString();
    }

    /**
     * Deserializes an event, converting it from a JSON string to an instance of the correct class
     *
     * @param serialized The serialized event
     * @return The event
     * @throws SerializationException if the string is not a correct JSON or the event is invalid
     */
    @Override
    public AbstractEvent deserialize(String serialized) throws SerializationException {
        JsonElement parsed;

        try {
            parsed = JsonParser.parseString(serialized);
        } catch (JsonSyntaxException exception) {
            throw new SerializationException(exception.getMessage());
        }

        if (!parsed.isJsonObject()) {
            throw new SerializationException("Not a JSON object: " + parsed.toString());
        }

        JsonObject json = parsed.getAsJsonObject();

        Class<AbstractEvent> eventClass = getEventClass(json);
        JsonElement attributes = json.get(PROPERTY_ATTRIBUTES);

        if (attributes == null) {
            throw new SerializationException(NO_ATTRIBUTES);
        }

        try {
            return gson.fromJson(attributes, eventClass);
        } catch (JsonSyntaxException exception) {
            throw new SerializationException(exception.getMessage());
        }
    }

    /**
     * Obtains the class of an event from its serialized JSON string
     *
     * @param json The serialized event
     * @return The class of the event
     * @throws SerializationException if no class is found for the event
     */
    @SuppressWarnings("unchecked")
    private Class<AbstractEvent> getEventClass(JsonObject json) throws SerializationException {
        try {
            JsonElement eventElement = json.get(PROPERTY_EVENT);

            if (eventElement == null) {
                throw new SerializationException(NULL_EVENT);
            }

            String event = eventElement.getAsString();
            String classPackage = EVENTS_PACKAGE;
            if (event.startsWith("Lobby")) {
                classPackage += "lobby.";
            } else if (event.startsWith("Request")) {
                classPackage += "request.";
            } else if (event.startsWith("Response")) {
                classPackage += "response.";
            }

            Class<?> eventClass = Class.forName(classPackage + event);

            if (!AbstractEvent.class.isAssignableFrom(eventClass)) {
                throw new SerializationException(INVALID_EVENT);
            }

            return (Class<AbstractEvent>) eventClass;
        } catch (ClassNotFoundException | ClassCastException | IllegalStateException exception) {
            throw new SerializationException(exception.getMessage());
        }
    }

}
