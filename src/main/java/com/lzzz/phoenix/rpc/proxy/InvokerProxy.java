package com.lzzz.phoenix.rpc.proxy;

import com.lzzz.phoenix.common.model.RpcRequest;
import com.lzzz.phoenix.common.util.ProtocolUtils;
import com.lzzz.phoenix.rpc.ReferenceContext;
import com.lzzz.phoenix.rpc.handler.ReferenceInvokeHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

public class InvokerProxy<T> implements InvocationHandler {
    private Class<T> clazz;
    private String version;

    public InvokerProxy(Class<T> clazz, String version) {
        this.clazz = clazz;
        this.version = version;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        RpcRequest request = new RpcRequest();
        request.setId(UUID.randomUUID().toString());
        request.setClassName(clazz.getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getExceptionTypes());
        request.setParameters(args);
        request.setVersion(version);

        String serviceKey = ProtocolUtils.makeServiceKey(clazz.getName(), version);
        ReferenceInvokeHandler invokeHandler = ReferenceContext.getInstance().selectService(serviceKey);
        return invokeHandler.sendRequest(request).get();
    }
}
