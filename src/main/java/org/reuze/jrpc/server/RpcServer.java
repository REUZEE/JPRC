package org.reuze.jrpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;
import org.reuze.jrpc.protocol.RpcRequest;
import org.reuze.jrpc.protocol.RpcResponse;
import org.reuze.jrpc.protocol.codec.RpcDecoder;
import org.reuze.jrpc.protocol.codec.RpcEncoder;
import org.reuze.jrpc.protocol.serialize.JsonSerializer;
import org.reuze.jrpc.service.RpcHandler;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Reuze
 * @Date 05/01/2023
 */
@Slf4j
public class RpcServer {

    private String ip;
    private int port;
    private InetSocketAddress ipPort;
    private ServerBootstrap serverBootstrap;
    private Channel channel;
    private ChannelFuture channelFuture;
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    public Map<Class<?>, Object> interface2Impl = new ConcurrentHashMap<>();

    public RpcServer(String ip, int port) {
        this.ip = ip;
        this.port = port;
        ipPort = new InetSocketAddress(ip, port);
    }

    public boolean start() {
        try {
            init();
            log.info("RpcServer starts [{}:{}]", ip, port);
            this.channel = this.serverBootstrap.bind(ipPort).sync().channel().closeFuture().channel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void init() {
        this.serverBootstrap = new ServerBootstrap();
        this.serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class);
        this.serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast(new LengthFieldBasedFrameDecoder(65535,0,4));
                pipeline.addLast("decoder", new RpcDecoder(RpcRequest.class, new JsonSerializer()));
                pipeline.addLast("encoder", new RpcEncoder(RpcResponse.class, new JsonSerializer()));
                pipeline.addLast("handler", new RpcHandler(interface2Impl));
            }
        });
    }

    public void register(Object impl) {
        Class<?>[] interfaces = impl.getClass().getInterfaces();
        for (Class<?> in : interfaces) {
            log.info("Register [{}] for [{}]", impl.getClass().getName(), in.getName());
            interface2Impl.putIfAbsent(in, impl);
        }
    }

    public void stop() {
        if (channel != null) {
            channel.close();
            channel.parent().close();
        }
    }
}
