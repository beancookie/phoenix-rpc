package com.lzzz.phoenix.rpc.handler;

import com.lzzz.phoenix.common.model.RpcRequest;
import com.lzzz.phoenix.common.model.RpcResponse;
import com.lzzz.phoenix.common.util.ProtocolUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ServiceInvokeHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private final Map<String, Object> invokerMap;

    public ServiceInvokeHandler(Map<String, Object> handlerMap) {
        this.invokerMap = handlerMap;
    }

    protected Optional<Object> invoke(RpcRequest rpcRequest) throws InvocationTargetException {
        Object invokerBean = invokerMap.get(ProtocolUtils.makeServiceKey(rpcRequest.getClassName(), rpcRequest.getVersion()));
        if (Objects.isNull(invokerBean)) {
            return Optional.empty();
        }

        FastClass invokerProxy = FastClass.create(invokerBean.getClass());

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
}
