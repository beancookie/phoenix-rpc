package com.lzzz.phoenix.rpc.handler;

import com.lzzz.phoenix.common.model.ResponseFuture;
import com.lzzz.phoenix.common.model.RpcRequest;
import com.lzzz.phoenix.common.model.RpcResponse;
import com.lzzz.phoenix.rpc.RequestSender;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ReferenceInvokeHandler extends SimpleChannelInboundHandler<RpcResponse> implements RequestSender {
    private Channel channel;

    private ConcurrentMap<String, ResponseFuture> futureHolder;

    public Channel getChannel() {
        return channel;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
        this.futureHolder = new ConcurrentHashMap<>();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse rpcResponse) {
        ResponseFuture responseFuture = futureHolder.get(rpcResponse.getRequestId());
        if (Objects.nonNull(responseFuture)) {
            responseFuture.setResponse(rpcResponse);
            futureHolder.remove(rpcResponse.getRequestId());
        }
    }

    @Override
    public ResponseFuture sendRequest(RpcRequest rpcRequest) throws InterruptedException {
        ResponseFuture responseFuture = new ResponseFuture();
        ChannelFuture sendFuture = this.channel.writeAndFlush(rpcRequest).sync();
        if (Objects.nonNull(sendFuture) && sendFuture.isSuccess()) {
            futureHolder.put(rpcRequest.getId(), responseFuture);
        }
        return responseFuture;
    }
}
