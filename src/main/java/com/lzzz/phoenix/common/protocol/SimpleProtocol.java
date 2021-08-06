package com.lzzz.phoenix.common.protocol;

import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleProtocol that = (SimpleProtocol) o;
        return Objects.equals(host, that.host) && Objects.equals(port, that.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port);
    }
}
