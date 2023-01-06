package org.reuze.jrpc.service;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.reuze.jrpc.protocol.RpcRequest;
import org.reuze.jrpc.protocol.RpcResponse;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Reuze
 * @Date 05/01/2023
 */
@ChannelHandler.Sharable
@Slf4j
public class RpcHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private Map interface2Impl;

    public RpcHandler(Map interface2Impl) {
        this.interface2Impl = interface2Impl;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest rpcRequest) {
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(rpcRequest.getRequestId());
        try {
            Object handler = handler(rpcRequest);
            rpcResponse.setResult(handler);
        } catch (Exception e) {
            rpcResponse.setThrowable(e);
            e.printStackTrace();
        }
        ctx.writeAndFlush(rpcResponse);
        log.info("Return RpcResponse: {}", rpcResponse.toString());
    }

    private Object handler(RpcRequest rpcRequest) throws Exception {
        log.info("Receive RpcRequest: {}", rpcRequest.toString());
        Class<?> clz = Class.forName(rpcRequest.getClassName());
        Object object = interface2Impl.get(clz);
        Method method = clz.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
        return method.invoke(object, rpcRequest.getParameter());
    }
}
