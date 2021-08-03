package com.lzzz.phoenix.rpc.initializer;

import com.alibaba.nacos.api.exception.NacosException;
import com.lzzz.phoenix.common.annotation.PhoenixReference;
import com.lzzz.phoenix.rpc.ReferenceContext;
import com.lzzz.phoenix.rpc.discovery.NacosServiceDiscovery;
import com.lzzz.phoenix.rpc.discovery.ServiceDiscovery;
import com.lzzz.phoenix.rpc.proxy.InvokerProxy;
import com.lzzz.phoenix.rpc.proxy.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Objects;

public class ReferenceInitializer implements ProxyFactory<Object>, BeanPostProcessor, InitializingBean {
    protected final ReferenceContext referenceContext = ReferenceContext.getInstance();

    private ServiceDiscovery serviceDiscovery;

    public ReferenceInitializer(String registryAddress) {
        try {
            serviceDiscovery = new NacosServiceDiscovery(registryAddress);
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            PhoenixReference reference = field.getAnnotation(PhoenixReference.class);
            if (Objects.nonNull(reference)) {
                try {
                    field.setAccessible(true);
                    if (reference.interfaceClass() == void.class) {
                        field.set(bean, createReference(field.getType(), reference.version()));
                    } else {
                        field.set(bean, createReference(reference.interfaceClass(), reference.version()));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }

    @Override
    public Object createReference(Class<?> interfaceClass, String version) {
        return Proxy.newProxyInstance(
            interfaceClass.getClassLoader(),
            new Class<?>[] {interfaceClass},
            new InvokerProxy<>(interfaceClass, version)
        );
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        serviceDiscovery.discoveryAll().forEach(referenceContext::connectService);
        serviceDiscovery.subscribe();
    }

}
