package org.reuze.jrpc.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.reuze.jrpc.protocol.RpcMessage;
import org.reuze.jrpc.protocol.serialize.Serializer;
import org.reuze.jrpc.protocol.serialize.SerializerFactory;

/**
 * @author Reuze
 * @Date 05/01/2023
 */
public class RpcEncoder extends MessageToByteEncoder {

    private Class<?> clz;
    private Serializer serializer;

    public RpcEncoder() {}

    public RpcEncoder(Class<?> clz) {
        this.clz = clz;
    }

    public RpcEncoder(Class<?> clz, Serializer serializer) {
        this.clz = clz;
        this.serializer = serializer;
    }

    protected void encode(ChannelHandlerContext channelHandlerContext, Object msg, ByteBuf out) throws Exception {
        if (msg instanceof RpcMessage) {
            Serializer serializer = SerializerFactory.getSerializer(((RpcMessage) msg).getSerializeType());
            byte[] bytes = serializer.serialize(msg);
            out.writeInt(bytes.length);
            out.writeByte(((RpcMessage) msg).getRpcType());
            out.writeByte(((RpcMessage) msg).getSerializeType());
            out.writeBytes(bytes);
        }
    }
}
