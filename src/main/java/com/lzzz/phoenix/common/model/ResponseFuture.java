package com.lzzz.phoenix.common.model;

import java.util.Objects;
import java.util.concurrent.*;

public class ResponseFuture implements Future<Object> {
    private final CountDownLatch latch = new CountDownLatch(1);

    private volatile RpcResponse rpcResponse;

    public void setResponse(RpcResponse rpcResponse) {
        this.rpcResponse = rpcResponse;
        latch.countDown();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        latch.await();
        if (Objects.nonNull(rpcResponse.getException())) {
            throw new ExecutionException(rpcResponse.getException());
        }
        return rpcResponse.getResult();
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        throw new UnsupportedOperationException();
    }
}
