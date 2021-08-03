package com.lzzz.phoenix.rpc.example.client.controller;

import com.lzzz.phoenix.common.annotation.PhoenixReference;
import com.lzzz.phoenix.rpc.example.model.HelloDTO;
import com.lzzz.phoenix.rpc.example.service.HelloService;
import com.lzzz.phoenix.rpc.example.service.MonitorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RestController
public class HelloController {
    @PhoenixReference
    private HelloService helloService;

    @PhoenixReference
    private MonitorService monitorService;

    private ThreadPoolExecutor pool = new ThreadPoolExecutor(500, 1000, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10000));

    @GetMapping("/hello")
    public long sayHello() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(10000);
        for (int i = 0; i < 10000; i++) {
            System.out.println(i);
            pool.submit(() -> {
                HelloDTO helloDTO = helloService.sayHello();
                System.out.println(helloDTO);
                latch.countDown();
            });
        }
        latch.await();
        System.out.println(System.currentTimeMillis() - startTime);
        return System.currentTimeMillis() - startTime;
    }


    @GetMapping("/listen")
    public String listen() {
        return monitorService.listen();
    }
}
