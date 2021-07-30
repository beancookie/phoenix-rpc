package com.lzzz.phoenix.rpc.example.client.controller;

import com.lzzz.phoenix.common.annotation.PhoenixReference;
import com.lzzz.phoenix.common.model.RpcResponse;
import com.lzzz.phoenix.rpc.example.model.HelloDTO;
import com.lzzz.phoenix.rpc.example.service.HelloService;
import com.lzzz.phoenix.rpc.example.service.MonitorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @PhoenixReference
    private HelloService helloService;

    @PhoenixReference
    private MonitorService monitorService;

    @GetMapping("/hello")
    public HelloDTO sayHello() {
        return helloService.sayHello();
    }


    @GetMapping("/listen")
    public String listen() {
        return monitorService.listen();
    }
}
