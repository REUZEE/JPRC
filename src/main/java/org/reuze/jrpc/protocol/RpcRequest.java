package org.reuze.jrpc.protocol;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Reuze
 * @Date 05/01/2023
 */
@Setter
@Getter
@ToString
public class RpcRequest {
    private String requestId;
    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameter;
}
