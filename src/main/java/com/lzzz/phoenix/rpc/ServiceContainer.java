package com.lzzz.phoenix.rpc;

import com.lzzz.phoenix.common.protocol.SimpleProtocol;
import com.lzzz.phoenix.rpc.handler.ReferenceInvokeHandler;

public interface ServiceContainer {
    /**
     *
     * @param protocol
     * @param invokeHandler
     */
    void addService(SimpleProtocol protocol);

    /**
     *
     * @param protocol
     */
    void removeService(SimpleProtocol protocol);
}
