package org.reuze.jrpc.protocol.serialize;

/**
 * @author Reuze
 * @Date 05/01/2023
 */
public interface Serializer {
    <T> byte[] serialize(T obj);
    <T> T deSerialize(byte[] data, Class<T> clz);
}
