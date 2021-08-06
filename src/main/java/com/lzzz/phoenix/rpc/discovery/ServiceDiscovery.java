package com.lzzz.phoenix.rpc.discovery;

import com.lzzz.phoenix.common.protocol.SimpleProtocol;

import java.util.List;

public interface ServiceDiscovery {
    /**
     *
     * @return
     * @throws Exception
     */
    SimpleProtocol discovery() throws Exception;

    /**
     *
     * @return
     * @throws Exception
     */
    List<SimpleProtocol> discoveryAll() throws Exception;

    /**
     *
     */
    void subscribe() throws Exception;
}
