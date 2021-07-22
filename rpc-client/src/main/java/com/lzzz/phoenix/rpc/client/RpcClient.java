package com.lzzz.phoenix.rpc.client;

import com.lzzz.phoenix.rpc.common.codec.RpcEncoder;
import com.lzzz.phoenix.rpc.common.model.RpcRequest;
import com.lzzz.phoenix.rpc.common.serializer.JsonSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author: LuZhong
 * @date: 2021/07/19
 */
public class RpcClient {
    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup(4))
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                ctx.write(new RpcRequest());
                            }
                        });
                        ch.pipeline().addLast(new RpcEncoder(RpcRequest.class, new JsonSerializer<>()));
                    }
                });

        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6789);
        channelFuture.channel().closeFuture().sync();
    }
}
