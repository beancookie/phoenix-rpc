package com.lzzz.phoenix.rpc.route;

import com.lzzz.phoenix.common.exception.ServiceNotFoundException;
import com.lzzz.phoenix.common.protocol.SimpleProtocol;

public interface LoadBalance {
    SimpleProtocol route(String key) throws ServiceNotFoundException;
}
