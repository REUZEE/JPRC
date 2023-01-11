package org.reuze.jrpc.protocol;

import lombok.Getter;
import lombok.Setter;
import org.reuze.jrpc.common.RpcType;

/**
 * @author Reuze
 * @Date 09/01/2023
 */
@Setter
@Getter
public class RpcPing extends RpcMessage {
    private byte rpcType = RpcType.PING.getValue();
}
