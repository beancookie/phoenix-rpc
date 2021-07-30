package com.lzzz.phoenix.rpc;

import com.lzzz.phoenix.common.codec.RpcDecoder;
import com.lzzz.phoenix.common.codec.RpcEncoder;
import com.lzzz.phoenix.common.exception.ServiceNotFoundException;
import com.lzzz.phoenix.common.model.RpcRequest;
import com.lzzz.phoenix.common.model.RpcResponse;
import com.lzzz.phoenix.common.protocol.SimpleProtocol;
import com.lzzz.phoenix.common.serializer.kryo.KryoSerializer;
import com.lzzz.phoenix.rpc.handler.ReferenceInvokeHandler;
import com.lzzz.phoenix.rpc.route.LoadBalance;
import com.lzzz.phoenix.rpc.route.SimpleLoadBalance;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class ReferenceContext implements ServiceSelector, ServiceContainer, ServiceConnector {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReferenceContext.class);

    private final ThreadPoolExecutor threadPoolExecutor;
    private EventLoopGroup eventLoopGroup;

    private final ConcurrentMap<SimpleProtocol, ReferenceInvokeHandler> onlineServiceMap;
    private final LoadBalance loadBalance;

    private ReferenceContext() {
        this.onlineServiceMap = new ConcurrentHashMap<>();
        this.loadBalance = new SimpleLoadBalance(onlineServiceMap);
        this.threadPoolExecutor = new ThreadPoolExecutor(
            4,
            8,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(1000)
        );
        this.eventLoopGroup = new NioEventLoopGroup(4);
    }

    @Override
    public void addService(SimpleProtocol protocol) {
        this.connectService(protocol);
    }

    @Override
    public void removeService(SimpleProtocol protocol) {
        ReferenceInvokeHandler invokeHandler = onlineServiceMap.get(protocol);
        invokeHandler.getChannel().close();
        onlineServiceMap.remove(protocol);
    }

    @Override
    public void connectService(SimpleProtocol protocol) {
        threadPoolExecutor.submit(() -> {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                            .addLast(new RpcEncoder(RpcRequest.class, new KryoSerializer()))
                            .addLast(new RpcDecoder(RpcResponse.class, new KryoSerializer()))
                            .addLast(new ReferenceInvokeHandler());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect(protocol.getHost(), protocol.getPort());
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(final ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        ReferenceInvokeHandler handler = channelFuture.channel().pipeline().get(ReferenceInvokeHandler.class);
                        onlineServiceMap.put(protocol, handler);
                    }
                }
            });
        });
    }

    private static class SingletonHolder {
        private static final ReferenceContext INSTANCE = new ReferenceContext();
    }

    public static ReferenceContext getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public ReferenceInvokeHandler selectService(String key) throws ServiceNotFoundException {
        return onlineServiceMap.get(loadBalance.route(key));
    }
}
