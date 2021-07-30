package com.lzzz.phoenix.rpc.discovery;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.lzzz.phoenix.common.exception.ServiceNotFoundException;
import com.lzzz.phoenix.common.protocol.ServiceProtocol;
import com.lzzz.phoenix.common.protocol.SimpleProtocol;
import com.lzzz.phoenix.common.util.ProtocolUtils;
import com.lzzz.phoenix.rpc.ReferenceContext;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.lzzz.phoenix.common.constant.ProtocolConstants.*;
import static com.lzzz.phoenix.common.constant.ServiceNamespaceConstants.*;


public class NacosServiceDiscovery implements ServiceDiscovery {
    private NamingService namingService;
    private ReferenceContext referenceContext;

    public NacosServiceDiscovery(String registryAddress) throws NacosException {
        this.referenceContext = ReferenceContext.getInstance();
        namingService = NamingFactory.createNamingService(registryAddress);
    }

    private Set<ServiceProtocol> convertServiceProtocol(String serviceProtocolsMeta) {
        return Stream.of(serviceProtocolsMeta.split(SERVICE_SPLIT))
            .map(serviceProtocolMeta -> ProtocolUtils.resolveServiceProtocol(serviceProtocolsMeta))
            .collect(Collectors.toSet());
    }

    private SimpleProtocol buildProtocol(Instance instance) {
        SimpleProtocol protocol = new SimpleProtocol();
        protocol.setHost(instance.getIp());
        protocol.setPort(instance.getPort());
        protocol.setServiceProtocols(convertServiceProtocol(instance.getMetadata().get(SERVICE_PROTOCOLS_KRY)));

        return protocol;
    }

    @Override
    public SimpleProtocol discovery() throws NacosException, ServiceNotFoundException {
        Instance instance = namingService.selectOneHealthyInstance(SERVICE_NAME);
        if (Objects.isNull(instance)) {
            throw new ServiceNotFoundException();
        }
        return buildProtocol(instance);
    }

    @Override
    public List<SimpleProtocol> discoveryAll() throws Exception {
        return namingService.selectInstances(SERVICE_NAME, true)
            .stream()
            .map(this::buildProtocol)
            .collect(Collectors.toList());
    }

    @Override
    public void subscribe() throws NacosException {
        namingService.subscribe(SERVICE_NAME, event -> {
            if (event instanceof NamingEvent) {
                List<Instance> instanceList = ((NamingEvent) event).getInstances();
                instanceList.forEach(instance -> {
                    if (instance.isHealthy()) {
                        referenceContext.addService(buildProtocol(instance));
                    } else {
                        referenceContext.removeService(buildProtocol(instance));
                    }
                });
            }
        });
    }
}
