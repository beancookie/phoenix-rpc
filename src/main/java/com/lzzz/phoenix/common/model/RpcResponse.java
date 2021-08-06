package com.lzzz.phoenix.common.model;

import java.io.Serializable;

/**
 * @author: LuZhong
 * @date: 2021/07/19
 */
public class RpcResponse implements Serializable {
    private Long requestId;
    private Exception exception;
    private Object result;

    public boolean isError() {
        return exception != null;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
