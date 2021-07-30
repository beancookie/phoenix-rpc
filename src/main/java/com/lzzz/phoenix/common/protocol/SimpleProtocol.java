package com.lzzz.phoenix.common.protocol;

import java.util.Set;

public class SimpleProtocol {
    private String host;
    private Integer port;

    private Set<ServiceProtocol> serviceProtocols;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Set<ServiceProtocol> getServiceProtocols() {
        return serviceProtocols;
    }

    public void setServiceProtocols(Set<ServiceProtocol> serviceProtocols) {
        this.serviceProtocols = serviceProtocols;
    }
}
