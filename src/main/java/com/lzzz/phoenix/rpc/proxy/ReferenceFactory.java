package com.lzzz.phoenix.rpc.proxy;

import java.lang.reflect.InvocationHandler;

public interface ReferenceFactory {
    Object createReference(Class<?> interfaceClass, String version, Object target) throws Throwable;
}
