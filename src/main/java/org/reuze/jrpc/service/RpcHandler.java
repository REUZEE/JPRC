package org.reuze.jrpc.service;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.reuze.jrpc.protocol.RpcType;
import org.reuze.jrpc.protocol.RpcMessage;
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
public class RpcHandler extends SimpleChannelInboundHandler<RpcMessage> {

    private Map interface2Impl;

    public RpcHandler(Map interface2Impl) {
        this.interface2Impl = interface2Impl;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMessage rpcMessage) {
        if (rpcMessage.getRpcType() == RpcType.PING.getValue()) {
            log.info("Receive heartbeat ...");
            return;
        }
        RpcRequest rpcRequest = (RpcRequest) rpcMessage;
        log.info("Receive rpcRequest: {}", rpcRequest.toString());
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

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                log.info("Idle check, close the connection");
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    private Object handler(RpcRequest rpcRequest) throws Exception {
        log.info("Receive RpcRequest: {}", rpcRequest.toString());
        Class<?> clz = Class.forName(rpcRequest.getClassName());
        Object object = interface2Impl.get(clz);
        Method method = clz.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
        return method.invoke(object, rpcRequest.getParameter());
    }
}
