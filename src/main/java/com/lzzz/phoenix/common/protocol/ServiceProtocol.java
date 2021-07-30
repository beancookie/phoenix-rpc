package com.lzzz.phoenix.common.protocol;

public class ServiceProtocol {
    private String serviceKey;
    private String serviceName;
    private String version;

    public String getServiceKey() {
        return serviceKey;
    }

    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
