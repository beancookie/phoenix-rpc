package com.lzzz.phoenix.rpc.example.server.server.service;

import com.lzzz.phoenix.common.annotation.PhoenixService;
import com.lzzz.phoenix.rpc.example.model.HelloDTO;
import com.lzzz.phoenix.rpc.example.service.HelloService;
import com.lzzz.phoenix.rpc.example.service.MonitorService;
import org.springframework.stereotype.Service;

@Service
@PhoenixService(interfaceClass = MonitorService.class)
public class MonitorServiceImpl implements MonitorService {

    @Override
    public String listen() {
        return "listening";
    }
}
