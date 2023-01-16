package org.reuze.jrpc.common.proxy;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.reuze.jrpc.client.RpcClient;
import org.reuze.jrpc.common.URL;
import org.reuze.jrpc.common.loadbalance.LoadBalance;
import org.reuze.jrpc.common.loadbalance.RandomLoadBalancer;
import org.reuze.jrpc.protocol.RpcRequest;
import org.reuze.jrpc.protocol.serialize.SerializerType;
import org.reuze.jrpc.registry.Registry;
import org.reuze.jrpc.registry.zk.ZkRegistry;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

/**
 * @author Reuze
 * @Date 05/01/2023
 */
@Slf4j
public class RpcInvoker<T> implements InvocationHandler {

    private Class<T> clz;
    private RpcClient rpcClient;
    private Registry registry;
    private LoadBalance loadBalance;

    public RpcInvoker(Class<T> clz) {
        this.clz = clz;
        this.registry = new ZkRegistry();
        this.loadBalance = new RandomLoadBalancer();
        this.rpcClient = RpcClient.getInstance();
    }

    @Deprecated
    public RpcInvoker(RpcClient rpcClient, Class<T> clz) {
        this.clz = clz;
        this.rpcClient = rpcClient;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setSerializeType(SerializerType.KRYO_SERIALIZER.getValue());
        rpcRequest.setRequestId(UUID.randomUUID().toString());
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameterTypes(method.getParameterTypes());
        rpcRequest.setParameter(args);

        URL condition = new URL();
        Channel channel;
        condition.setServiceName(rpcRequest.getClassName());
        int retryTimes = 3;
        do {
            if (--retryTimes < 0) {
                return null;
            }
            List<URL> urls = registry.lookup(condition);
            if (urls.isEmpty()) {
                log.info("No service registered");
                return null;
            }
            URL selected = loadBalance.select(urls);
            channel = rpcClient.getChannel(selected.getIp(), selected.getPort());
        } while (channel == null || !channel.isActive());

        return rpcClient.send(rpcRequest, channel);
    }
}
