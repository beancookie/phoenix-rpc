package com.lzzz.phoenix.rpc.example.client.config;

import com.lzzz.phoenix.rpc.ReferenceInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RpcConfig {
    @Bean
    public ReferenceInitializer nettyServerAware() {
        return new ReferenceInitializer("127.0.0.1:8848");
    }
}
