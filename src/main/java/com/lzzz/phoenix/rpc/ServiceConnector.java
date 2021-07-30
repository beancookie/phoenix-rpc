package com.lzzz.phoenix.rpc;

import com.lzzz.phoenix.common.protocol.SimpleProtocol;

public interface ServiceConnector {
    void connectService(SimpleProtocol protocol);
}
