package com.lzzz.phoenix;

import com.lzzz.phoenix.rpc.ServiceInitializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
@ConditionalOnClass(ServiceInitializer.class)
@EnableConfigurationProperties(ConfigurationProperties.class)
public class PhoenixServerAutoConfiguration {
    @Resource
    private PhoenixServerProperties phoenixServerProperties;

    @Bean
    @ConditionalOnMissingBean(ServiceInitializer.class)
    public ServiceInitializer nettyServerAware() {
        return new ServiceInitializer(phoenixServerProperties.getHost(), phoenixServerProperties.getPort(), phoenixServerProperties.getRegistryAddress());
    }
}
