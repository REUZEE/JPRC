package org.reuze.jrpc.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Reuze
 * @Date 09/01/2023
 */
@Getter
@AllArgsConstructor
public enum RpcType {

    REQUEST((byte) 1),

    RESPONSE((byte) 2),

    PING((byte) 3),

    PONG((byte) 4);

    private byte value;

}
