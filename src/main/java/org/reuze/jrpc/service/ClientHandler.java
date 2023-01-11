package org.reuze.jrpc.service;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.reuze.jrpc.common.DefaultFuture;
import org.reuze.jrpc.protocol.RpcPing;
import org.reuze.jrpc.protocol.RpcRequest;
import org.reuze.jrpc.protocol.RpcResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Reuze
 * @Date 05/01/2023
 */
@Slf4j
public class ClientHandler extends ChannelDuplexHandler {

    private final Map<String, DefaultFuture> futureMap = new ConcurrentHashMap<>();

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof RpcRequest) {
            RpcRequest rpcRequest = (RpcRequest) msg;
            // log.info("Send rpcRequest: {}", rpcRequest);
            // futureMap.putIfAbsent(rpcRequest.getRequestId(), new DefaultFuture());
        }
        super.write(ctx, msg, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("Receive msg: {}", msg.toString());
        if (msg instanceof RpcResponse) {
            RpcResponse rpcResponse = (RpcResponse) msg;
            log.info("Receive RpcResponse: {}", rpcResponse.toString());
            DefaultFuture defaultFuture = futureMap.get(rpcResponse.getRequestId());
            defaultFuture.setRpcResponse(rpcResponse);
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                RpcPing rpcPing = new RpcPing();
                ctx.channel().writeAndFlush(rpcPing);
                log.info("Send heartbeat ...");
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    public void setDefaultFuture(String requestId, DefaultFuture defaultFuture) {
        futureMap.putIfAbsent(requestId, new DefaultFuture());
    }

    public RpcResponse getRpcResponse(String requestId) {
        try {
            DefaultFuture defaultFuture = futureMap.get(requestId);
            return defaultFuture.getRpcResponse(10);
        } finally {
            futureMap.remove(requestId);
        }
    }

}
