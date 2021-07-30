package com.lzzz.phoenix.rpc.example.server.server.service;

import com.lzzz.phoenix.common.annotation.PhoenixService;
import com.lzzz.phoenix.rpc.example.model.HelloDTO;
import com.lzzz.phoenix.rpc.example.service.HelloService;
import org.springframework.stereotype.Service;

@Service
@PhoenixService
public class HelloServiceImpl implements HelloService {
    public HelloDTO sayHello() {
        return new HelloDTO(1, "abc");
    }
}
