package com.lzzz.phoenix.rpc.proxy.javassist;

import java.lang.reflect.InvocationHandler;

public interface ProxyFactory {
    <T> T createReference(Class<?> targetClass, InvocationHandler handler) throws Throwable;
}
