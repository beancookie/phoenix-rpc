package com.lzzz.phoenix.rpc;

/**
 * @author: LuZhong
 * @date: 2021/07/19
 */
public interface Server {
    /**
     *
     */
    void start();

    /**
     *
     */
    void stop();

    /**
     *
     * @param serviceClazz
     */
    void addService(Class<?> serviceClazz);
}
