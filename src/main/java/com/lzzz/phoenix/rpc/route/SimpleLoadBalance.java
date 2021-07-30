package com.lzzz.phoenix.rpc.route;

import com.lzzz.phoenix.common.exception.ServiceNotFoundException;
import com.lzzz.phoenix.common.protocol.SimpleProtocol;
import com.lzzz.phoenix.rpc.handler.ReferenceInvokeHandler;

import java.util.concurrent.ConcurrentMap;

public class SimpleLoadBalance implements LoadBalance {
    private final ConcurrentMap<SimpleProtocol, ReferenceInvokeHandler> onlineServiceMap;

    public SimpleLoadBalance(ConcurrentMap<SimpleProtocol, ReferenceInvokeHandler> onlineServiceMap) {
        this.onlineServiceMap = onlineServiceMap;
    }

    @Override
    public SimpleProtocol route(String key) throws ServiceNotFoundException {
        return onlineServiceMap.keySet()
            .stream()
            .filter(protocol -> protocol.getServiceProtocols().stream().anyMatch(service -> key.equals(service.getServiceKey())))
            .findAny()
            .orElseThrow(ServiceNotFoundException::new);
    }
}
