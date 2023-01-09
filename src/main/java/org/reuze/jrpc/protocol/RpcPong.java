package org.reuze.jrpc.protocol;

import org.reuze.jrpc.common.RpcType;

/**
 * @author Reuze
 * @Date 09/01/2023
 */
public class RpcPong extends RpcMessage {
    private byte rpcType = RpcType.PONG.getValue();
}
