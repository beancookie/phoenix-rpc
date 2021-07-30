package com.lzzz.phoenix.common.codec;

import com.lzzz.phoenix.common.serializer.Serializer;
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
public class RpcDecoder extends ByteToMessageDecoder {
    private static final Logger logger = LoggerFactory.getLogger(RpcDecoder.class);
    private final Class<?> genericClass;
    private final Serializer serializer;

    public RpcDecoder(Class<?> genericClass, Serializer serializer) {
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
