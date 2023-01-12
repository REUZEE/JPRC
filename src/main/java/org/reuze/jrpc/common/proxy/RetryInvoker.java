package org.reuze.jrpc.common.proxy;

import org.reuze.jrpc.protocol.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Reuze
 * @Date 12/01/2023
 */
public class RetryInvoker implements InvocationHandler {

    private static final Integer DEFAULT_RETRY_TIMES = 3;
    private RpcInvoker rpcInvoker;

    public RetryInvoker(RpcInvoker rpcInvoker) {
        this.rpcInvoker = rpcInvoker;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        int retryTimes = DEFAULT_RETRY_TIMES;
        for (int i = 0; i < retryTimes; i++) {
            RpcResponse result = (RpcResponse) rpcInvoker.invoke(proxy, method, args);
            if (result.getThrowable() == null) {
                return result.getResult();
            }
        }
        return null;
    }
}
