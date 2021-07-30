package com.lzzz.phoenix.rpc.example.client.controller;

import com.lzzz.phoenix.common.annotation.PhoenixReference;
import com.lzzz.phoenix.common.model.RpcResponse;
import com.lzzz.phoenix.rpc.example.model.HelloDTO;
import com.lzzz.phoenix.rpc.example.service.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @PhoenixReference
    private HelloService helloService;

    @GetMapping("/hello")
    public HelloDTO hello() {
        return helloService.sayHello();
    }
}
