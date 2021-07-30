package com.lzzz.phoenix.rpc;

import com.lzzz.phoenix.common.model.ResponseFuture;
import com.lzzz.phoenix.common.model.RpcRequest;

public interface RequestSender {
    ResponseFuture sendRequest(RpcRequest rpcRequest) throws InterruptedException;
}
