package com.lzzz.phoenix.rpc.registry;

import com.lzzz.phoenix.common.protocol.SimpleProtocol;

public interface ServiceRegistry {
    /**
     *
     * @param protocol
     */
    void register(SimpleProtocol protocol) throws Exception;

    /**
     *
     * @param protocol
     */
    void deregister(SimpleProtocol protocol) throws Exception;
}
