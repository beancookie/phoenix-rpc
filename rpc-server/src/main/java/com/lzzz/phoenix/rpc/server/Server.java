package com.lzzz.phoenix.rpc.server;

/**
 * @author: LuZhong
 * @date: 2021/07/19
 */
public interface Server {
    void start();
    void stop();

    void addService(Class<?> serviceClazz);
}
