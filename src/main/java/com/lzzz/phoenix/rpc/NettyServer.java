package com.lzzz.phoenix.rpc;

import com.alibaba.nacos.api.exception.NacosException;
import com.lzzz.phoenix.common.codec.RpcDecoder;
import com.lzzz.phoenix.common.codec.RpcEncoder;
import com.lzzz.phoenix.common.constant.IdleStateConstants;
import com.lzzz.phoenix.common.model.RpcRequest;
import com.lzzz.phoenix.common.model.RpcResponse;
import com.lzzz.phoenix.common.protocol.ServiceProtocol;
import com.lzzz.phoenix.common.protocol.SimpleProtocol;
import com.lzzz.phoenix.common.serializer.kryo.KryoSerializer;
import com.lzzz.phoenix.common.util.ProtocolUtils;
import com.lzzz.phoenix.rpc.handler.ServiceInvokeHandler;
import com.lzzz.phoenix.rpc.registry.NacosServiceRegistry;
import com.lzzz.phoenix.rpc.registry.ServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author: LuZhong
 * @date: 2021/07/19
 */
public class NettyServer implements Server {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);

    private final String host;
    private final Integer port;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private ServiceRegistry serviceRegistry;

    private final ConcurrentMap<String, Object> invokerMap = new ConcurrentHashMap<>();

    public NettyServer(String host, Integer port, String registryAddress) {
        this.host = host;
        this.port = port;

        try {
            this.serviceRegistry = new NacosServiceRegistry(registryAddress);
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    public void addService(String interfaceName, String version, Object serviceBean) {
        LOGGER.info("Adding service, interface: {}, version: {}, bean：{}", interfaceName, version, serviceBean);
        invokerMap.put(ProtocolUtils.makeServiceKey(interfaceName, version), serviceBean);
    }

    @Override
    public void start() {
        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                            .addLast(new IdleStateHandler(0, 0, IdleStateConstants.BEAT_TIMEOUT, TimeUnit.SECONDS))
                            .addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0))
                            .addLast(new RpcDecoder(RpcRequest.class, new KryoSerializer()))
                            .addLast(new RpcEncoder(RpcResponse.class, new KryoSerializer()))
                            .addLast(new ServiceInvokeHandler(invokerMap));
                        }
                    });

            ChannelFuture channelFuture = bootstrap.bind(host, port).sync();

            Set<ServiceProtocol> serviceProtocols = invokerMap
                .keySet().stream().map(ProtocolUtils::resolveServiceProtocol)
                .collect(Collectors.toSet());
            SimpleProtocol protocol = new SimpleProtocol();
            protocol.setHost(host);
            protocol.setPort(port);

            protocol.setServiceProtocols(serviceProtocols);
            serviceRegistry.register(protocol);

            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        this.workerGroup.shutdownGracefully();
        this.bossGroup.shutdownGracefully();
    }

    @Override
    public void addService(Class<?> serviceClazz) {
        invokerMap.put(serviceClazz.getName(), serviceClazz);
    }
}
