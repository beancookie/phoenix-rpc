package com.lzzz.phoenix.rpc;

import com.lzzz.phoenix.common.exception.ServiceNotFoundException;
import com.lzzz.phoenix.rpc.handler.ReferenceInvokeHandler;

public interface ServiceSelector {
    ReferenceInvokeHandler selectService(String key) throws ServiceNotFoundException;
}
