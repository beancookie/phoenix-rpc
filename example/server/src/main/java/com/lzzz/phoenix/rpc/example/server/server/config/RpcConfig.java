package com.lzzz.phoenix.rpc.example.server.server.config;

import com.lzzz.phoenix.rpc.initializer.ServiceInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RpcConfig {
    @Bean
    public ServiceInitializer nettyServerAware() {
        return new ServiceInitializer("127.0.0.1", 9998, "127.0.0.1:8848");
    }
}
