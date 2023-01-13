package org.reuze.jrpc.protocol.serialize;

/**
 * @author Reuze
 * @Date 13/01/2023
 */
public class SerializerFactory {

    public static Serializer getSerializer(byte value) {
        if (value == (byte) 1) {
            return new JsonSerializer();
        } else if (value == (byte) 2) {
            return new KryoSerializer();
        } else {
            return null;
        }
    }
}
