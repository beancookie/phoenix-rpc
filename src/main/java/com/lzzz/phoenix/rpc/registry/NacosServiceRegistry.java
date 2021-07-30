package com.lzzz.phoenix.rpc.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.lzzz.phoenix.common.protocol.SimpleProtocol;

import java.util.HashMap;
import java.util.Map;

import static com.lzzz.phoenix.common.constant.ProtocolConstants.*;
import static com.lzzz.phoenix.common.constant.ServiceNamespaceConstants.*;

public class NacosServiceRegistry implements ServiceRegistry {
    private NamingService namingService;

    public NacosServiceRegistry(String registryAddress) throws NacosException {
        namingService = NamingFactory.createNamingService(registryAddress);
    }

    private String convertServiceProtocol(SimpleProtocol protocol) {
        StringBuilder serviceProtocols = new StringBuilder();
        protocol.getServiceProtocols().forEach(serviceProtocol -> {
            serviceProtocols.append(serviceProtocol.getServiceName())
                    .append(SERVICE_NAME_VERSION_SPLIT)
                    .append(serviceProtocol.getVersion())
                    .append(SERVICE_SPLIT);
        });
        if (serviceProtocols.length() > 0) {
            serviceProtocols.deleteCharAt(serviceProtocols.length() - 1);
        }
        return serviceProtocols.toString();
    }

    @Override
    public void register(SimpleProtocol protocol) throws NacosException {
        Instance instance = new Instance();
        instance.setIp(protocol.getHost());
        instance.setPort(protocol.getPort());
        Map<String, String> metadata = new HashMap<>();
        metadata.put(SERVICE_PROTOCOLS_KRY, convertServiceProtocol(protocol));
        instance.setMetadata(metadata);
        namingService.registerInstance(SERVICE_NAME, instance);
    }

    @Override
    public void deregister(SimpleProtocol protocol) throws NacosException {
        namingService.deregisterInstance(SERVICE_NAME, protocol.getHost(), protocol.getPort());
    }
}
