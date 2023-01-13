package org.reuze.jrpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Reuze
 * @Date 09/01/2023
 */
@Getter
@AllArgsConstructor
public enum RpcType {

    REQUEST((byte) 1, RpcRequest.class),

    RESPONSE((byte) 2, RpcResponse.class),

    PING((byte) 3, RpcPing.class),

    PONG((byte) 4, RpcPong.class);

    private byte value;

    Class<?> clz;

    public static Class<?> getClass(byte value) {
        if (value == (byte) 1) {
            return REQUEST.getClz();
        } else if (value == (byte) 2) {
            return RESPONSE.getClz();
        } else if (value == (byte) 3) {
            return PING.getClz();
        } else if (value == (byte) 4) {
            return PONG.getClz();
        } else {
            return null;
        }
    }

}
