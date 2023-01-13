package org.reuze.jrpc.protocol;

import lombok.Getter;
import lombok.Setter;

import org.reuze.jrpc.protocol.serialize.SerializerType;

/**
 * @author Reuze
 * @Date 09/01/2023
 */
@Getter
@Setter
public class RpcMessage {

    private byte rpcType;

    private byte serializeType = SerializerType.JSON_SERIALIZER.getValue();

}
