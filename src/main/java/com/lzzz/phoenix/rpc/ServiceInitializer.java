package com.lzzz.phoenix.rpc;

import com.lzzz.phoenix.common.annotation.PhoenixService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;
import java.util.Objects;

public class ServiceInitializer extends NettyServer implements ApplicationContextAware, InitializingBean, DisposableBean {
    public ServiceInitializer(String host, Integer port, String registryAddress) {
        super(host, port, registryAddress);
    }

    @Override
    public void destroy() throws Exception {
        super.stop();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(PhoenixService.class);

        serviceBeanMap.forEach((name, bean) -> {
            PhoenixService service = bean.getClass().getAnnotation(PhoenixService.class);
            if (service.interfaceClass() == void.class) {
                super.addService(bean.getClass().getName(), service.version(), bean);
            } else {
                super.addService(service.interfaceClass().getName(), service.version(), bean);
            }
        });
    }
}
