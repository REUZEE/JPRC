package org.reuze.jrpc.protocol.serialize;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Reuze
 * @Date 13/01/2023
 */
@Getter
@AllArgsConstructor
public enum SerializerType {

    JSON_SERIALIZER((byte) 1),

    KRYO_SERIALIZER((byte) 2);

    private byte value;

}
