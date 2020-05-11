package it.polimi.ingsw.common.serializer;

public interface ISerializer<T> {

    String serialize(T object);

    T deserialize(String serialized) throws SerializationException;

}
