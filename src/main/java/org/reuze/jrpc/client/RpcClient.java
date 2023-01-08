package org.reuze.jrpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.reuze.jrpc.protocol.RpcRequest;
import org.reuze.jrpc.protocol.RpcResponse;
import org.reuze.jrpc.protocol.codec.RpcDecoder;
import org.reuze.jrpc.protocol.codec.RpcEncoder;
import org.reuze.jrpc.protocol.serialize.JsonSerializer;
import org.reuze.jrpc.service.ClientHandler;

import java.net.InetSocketAddress;

/**
 * @author Reuze
 * @Date 05/01/2023
 */
@Setter
@Getter
@Slf4j
public class RpcClient {

    private String ip;
    private int port;
    private InetSocketAddress ipPort;
    private EventLoopGroup eventLoopGroup;
    private Channel channel;
    private Bootstrap bootstrap;
    private ClientHandler clientHandler;

    public RpcClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.ipPort = new InetSocketAddress(ip, port);
        connect();
    }

    private void connect() {
        bootstrap = new Bootstrap();
        clientHandler = new ClientHandler();
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(65535,0,4));
                        pipeline.addLast("encoder", new RpcEncoder(RpcRequest.class, new JsonSerializer()));
                        pipeline.addLast("decoder", new RpcDecoder(RpcResponse.class, new JsonSerializer()));
                        pipeline.addLast("handler", clientHandler);
                    }
                });
        try {
            log.info("RpcClient starts, connects to [{}:{}]", ip, port);
            channel = bootstrap.connect(ipPort).sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        eventLoopGroup.shutdownGracefully();
        channel.closeFuture().awaitUninterruptibly();
    }

    public RpcResponse send(final RpcRequest rpcRequest) {
        try {
            channel.writeAndFlush(rpcRequest).await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return clientHandler.getRpcResponse(rpcRequest.getRequestId());
    }
}
