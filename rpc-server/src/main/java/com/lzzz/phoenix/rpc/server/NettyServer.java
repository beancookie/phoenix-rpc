package com.lzzz.phoenix.rpc.server;

import com.lzzz.phoenix.rpc.common.codec.RpcDecoder;
import com.lzzz.phoenix.rpc.common.model.RpcRequest;
import com.lzzz.phoenix.rpc.common.serializer.JsonSerializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: LuZhong
 * @date: 2021/07/19
 */
public class NettyServer implements Server {
    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private final String host;
    private final Integer port;

    public NettyServer(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    private final Map<String, Object> serviceMap = new ConcurrentHashMap<>();

    @Override
    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new RpcDecoder<>(RpcRequest.class, new JsonSerializer<>()));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture channelFuture = bootstrap.bind(host, port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void addService(Class<?> serviceClazz) {
        serviceMap.put(serviceClazz.getName(), serviceClazz);
    }
}
