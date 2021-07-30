package com.lzzz.phoenix;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("phoenix")
public class PhoenixServerProperties {
    private String host;
    private Integer port;
    private String registryAddress;

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

    public String getRegistryAddress() {
        return registryAddress;
    }

    public void setRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
    }
}
