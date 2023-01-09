package org.reuze.jrpc.protocol;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.reuze.jrpc.common.RpcType;

/**
 * @author Reuze
 * @Date 05/01/2023
 */
@Getter
@Setter
@ToString
public class RpcResponse extends RpcMessage{
    private byte rpcType = RpcType.RESPONSE.getValue();
    private String requestId;
    private Throwable throwable;
    private Object result;
}
