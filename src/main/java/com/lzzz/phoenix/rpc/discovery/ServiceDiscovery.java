package com.lzzz.phoenix.rpc.discovery;

import com.alibaba.nacos.api.exception.NacosException;
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
