package com.lzzz.phoenix.rpc.common.codec;

import com.lzzz.phoenix.rpc.common.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RPC Encoder
 *
 * @author luxiaoxun
 */
public class RpcEncoder<T> extends MessageToByteEncoder {
    private static final Logger logger = LoggerFactory.getLogger(RpcEncoder.class);
    private final Class<T> genericClass;
    private final Serializer<T> serializer;

    public RpcEncoder(Class<T> genericClass, Serializer<T> serializer) {
        this.genericClass = genericClass;
        this.serializer = serializer;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
        if (genericClass.isInstance(in)) {
            byte[] data = serializer.serialize((T) in);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
