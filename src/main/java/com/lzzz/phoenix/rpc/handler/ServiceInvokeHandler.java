package com.lzzz.phoenix.rpc.handler;

import com.lzzz.phoenix.common.model.RpcRequest;
import com.lzzz.phoenix.common.model.RpcResponse;
import com.lzzz.phoenix.common.util.ProtocolUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ServiceInvokeHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceInvokeHandler.class);

    private final Map<String, Object> invokerMap;

    private final ConcurrentMap<Class<?>, FastClass> invokerProxyCache = new ConcurrentHashMap<>();

    public ServiceInvokeHandler(Map<String, Object> handlerMap) {
        this.invokerMap = handlerMap;
    }

    private FastClass createProxy(Object invokerBean) {
        if (invokerProxyCache.containsKey(invokerBean.getClass())) {
            return invokerProxyCache.get(invokerBean.getClass());
        } else {
            FastClass invokerProxy = FastClass.create(invokerBean.getClass());
            invokerProxyCache.put(invokerBean.getClass(), invokerProxy);
            return invokerProxy;
        }
    }

    protected Optional<Object> invoke(RpcRequest rpcRequest) throws InvocationTargetException {
        Object invokerBean = invokerMap.get(ProtocolUtils.makeServiceKey(rpcRequest.getClassName(), rpcRequest.getVersion()));
        if (Objects.isNull(invokerBean)) {
            return Optional.empty();
        }

        FastClass invokerProxy = createProxy(invokerBean);

        Object result = invokerProxy.invoke(rpcRequest.getMethodName(), rpcRequest.getParameterTypes(), invokerBean, rpcRequest.getParameters());
        return Optional.ofNullable(result);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest rpcRequest) {
        RpcResponse response = new RpcResponse();
        try {
            Optional<Object> invokeResult = invoke(rpcRequest);
            response.setRequestId(rpcRequest.getId());
            response.setResult(invokeResult.orElseThrow(RuntimeException::new));
        } catch (Exception e) {
            response.setException(e);
        }
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        LOGGER.error("ServiceInvokeHandler error {}", cause.getMessage());
    }
}
