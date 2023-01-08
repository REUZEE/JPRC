package org.reuze.jrpc.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.reuze.jrpc.protocol.serialize.Serializer;

import java.util.List;

/**
 * @author Reuze
 * @Date 05/01/2023
 */
public class RpcDecoder extends ByteToMessageDecoder {

    private Class<?> clz;
    private Serializer serializer;

    public RpcDecoder(Class<?> clz, Serializer serializer) {
        this.clz = clz;
        this.serializer = serializer;
    }

    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }
        in.markReaderIndex();
        int dataLength = in.readInt();
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);
        Object object = serializer.deSerialize(data, clz);
        out.add(object);
    }
}
