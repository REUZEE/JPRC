package org.reuze.jrpc.common;

import org.reuze.jrpc.client.RpcClient;
import org.reuze.jrpc.protocol.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author Reuze
 * @Date 05/01/2023
 */
public class RpcInvoker<T> implements InvocationHandler {

    private Class<T> clz;
    private RpcClient rpcClient;

    public RpcInvoker(RpcClient rpcClient, Class<T> clz) {
        this.clz = clz;
        this.rpcClient = rpcClient;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setRequestId(UUID.randomUUID().toString());
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameterTypes(method.getParameterTypes());
        rpcRequest.setParameter(args);

        return rpcClient.send(rpcRequest).getResult();
    }
}
