package it.polimi.ingsw.common.serializer;

/**
 * An exception that represent an error during serialization or deserialization
 * Deserialization errors are caused by invalid serialized strings
 */
public class SerializationException extends Exception {

    /**
     * Class constructor
     *
     * @param message The exception message
     */
    public SerializationException(String message) {
        super(message);
    }

}
