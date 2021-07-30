package com.lzzz.phoenix.rpc.proxy;

public interface ProxyFactory<T> {
    T createReference(Class<?> interfaceClass, String version);
}
