package org.reuze.jrpc.common.proxy;

import org.reuze.jrpc.client.RpcClient;

import java.lang.reflect.Proxy;

/**
 * @author Reuze
 * @Date 05/01/2023
 */
public class JRpcProxy {

    @SuppressWarnings("unchecked")
    public static <T> T getProxy(RpcClient rpcClient, Class<T> clz) {
        return (T) Proxy.newProxyInstance(clz.getClassLoader(),
                new Class<?>[]{clz}, new RpcInvoker<T>(rpcClient, clz));
    }

    @SuppressWarnings("unchecked")
    public static <T> T getProxy(Class<T> clz) {
        return (T) Proxy.newProxyInstance(clz.getClassLoader(),
                new Class<?>[]{clz}, new RpcInvoker<T>(clz));
    }
}
