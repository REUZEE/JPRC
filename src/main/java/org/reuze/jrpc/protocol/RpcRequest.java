package org.reuze.jrpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.reuze.jrpc.protocol.serialize.SerializerType;

/**
 * @author Reuze
 * @Date 05/01/2023
 */
@Setter
@Getter
@ToString
public class RpcRequest extends RpcMessage {
    private byte rpcType = RpcType.REQUEST.getValue();
    private String requestId;
    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameter;
}
