package com.lzzz.phoenix.rpc.proxy.javassist;


import java.lang.reflect.InvocationHandler;

public class JavassistProxyFactory implements ProxyFactory {
    @Override
    public <T> T createReference(Class<?> targetClass, InvocationHandler handler) throws Throwable {
        return (T) ProxyGenerator.newProxyInstance(targetClass, handler);
    }
}
