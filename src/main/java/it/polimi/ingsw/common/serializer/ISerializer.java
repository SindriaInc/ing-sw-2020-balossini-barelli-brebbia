package it.polimi.ingsw.common.serializer;

/**
 * A Serializer must be able to convert from a <code>String</code> to an object of type <code>T</code> and vice-versa
 *
 * @param <T> The type of the object
 */
public interface ISerializer<T> {

    String serialize(T object);

    T deserialize(String serialized) throws SerializationException;

}
