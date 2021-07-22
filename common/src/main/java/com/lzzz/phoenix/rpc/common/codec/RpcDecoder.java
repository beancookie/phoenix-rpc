package com.lzzz.phoenix.rpc.common.codec;

import com.lzzz.phoenix.rpc.common.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author: LuZhong
 * @date: 2021/07/19
 */
public class RpcDecoder<T> extends ByteToMessageDecoder {
    private static final Logger logger = LoggerFactory.getLogger(RpcDecoder.class);
    private final Class<T> genericClass;
    private final Serializer<T> serializer;

    public RpcDecoder(Class<T> genericClass, Serializer<T> serializer) {
        this.genericClass = genericClass;
        this.serializer = serializer;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        byte[] data = new byte[in.readInt()];
        in.readBytes(data);

        out.add(serializer.deserialize(data, genericClass));
    }
}
