package org.reuze.jrpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.reuze.jrpc.common.URL;
import org.reuze.jrpc.protocol.RpcRequest;
import org.reuze.jrpc.protocol.RpcResponse;
import org.reuze.jrpc.protocol.codec.RpcDecoder;
import org.reuze.jrpc.protocol.codec.RpcEncoder;
import org.reuze.jrpc.protocol.serialize.JsonSerializer;
import org.reuze.jrpc.registry.Registry;
import org.reuze.jrpc.registry.zk.ZkRegistry;
import org.reuze.jrpc.registry.zk.RegistryFactory;
import org.reuze.jrpc.registry.zk.ZkRegistryFactory;
import org.reuze.jrpc.service.RpcHandler;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

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
    private Registry registry;
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
                pipeline.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                pipeline.addLast("decoder", new RpcDecoder(RpcRequest.class, new JsonSerializer()));
                pipeline.addLast("encoder", new RpcEncoder(RpcResponse.class, new JsonSerializer()));
                pipeline.addLast("handler", new RpcHandler(interface2Impl));
            }
        });

        this.connectRegistry();
    }

    public void register(Object impl) {
        URL url = new URL();
        url.setPort(this.port);
        url.setIp(this.ip);
        Class<?>[] interfaces = impl.getClass().getInterfaces();
        for (Class<?> in : interfaces) {
            log.info("Register [{}] for [{}]", impl.getClass().getName(), in.getName());
            interface2Impl.putIfAbsent(in, impl);
            url.setServiceName(in.getName());
            registry.register(url);
        }
    }

    private void connectRegistry() {
        RegistryFactory registryFactory = new ZkRegistryFactory();
        URL url = new URL();
        url.setIp(ZkRegistry.ZK_IP );
        url.setPort(ZkRegistry.ZK_PORT);
        registry = registryFactory.getRegistry(url);
    }

    public void stop() {
        if (channel != null) {
            channel.close();
            channel.parent().close();
        }
    }
}
