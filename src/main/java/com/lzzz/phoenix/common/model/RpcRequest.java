package com.lzzz.phoenix.common.model;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author: LuZhong
 * @date: 2021/07/19
 */
public class RpcRequest implements Serializable {

    private static final AtomicLong INVOKE_ID = new AtomicLong(0);

    private Long id;
    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;
    private String version;

    public RpcRequest() {
        this.id = INVOKE_ID.getAndIncrement();
    }

    public Long getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}