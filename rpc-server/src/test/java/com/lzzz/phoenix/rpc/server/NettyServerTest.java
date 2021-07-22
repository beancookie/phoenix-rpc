package com.lzzz.phoenix.rpc.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: LuZhong
 * @date: 2021/07/19
 */
class NettyServerTest {

    @Test
    void start() {
        new NettyServer("127.0.0.1", 6789).start();
    }
}