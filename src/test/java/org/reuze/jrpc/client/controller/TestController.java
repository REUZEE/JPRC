package org.reuze.jrpc.client.controller;

import org.reuze.jrpc.common.proxy.JRpcProxy;
import org.reuze.jrpc.service.Calculate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * @author Reuze
 * @Date 11/01/2023
 */
@RestController
public class TestController {

    @GetMapping("/test/add")
    public void add(@RequestParam int a, @RequestParam int b) {
        Calculate calculate = JRpcProxy.getProxy(Calculate.class);
        System.out.println(calculate.add(a, b));
    }

    /* ------------------------------------------------------
     * | 100 | 200 | 500 | 1000 |
     * ------------------------------------------------------
     * | 158 | 419 | 644 | 1068 |
     * -----------------------------------------------------*/
    @GetMapping("/test/add/auto")
    public String addAuto(@RequestParam int num) throws InterruptedException {
        Random random = new Random();
        int COUNT = num;
        System.out.println(COUNT);
        CountDownLatch countDownLatch = new CountDownLatch(COUNT);
        Calculate calculate = JRpcProxy.getProxy(Calculate.class);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            calculate.add(random.nextInt(1000), random.nextInt(1000));
            countDownLatch.countDown();
        }
        countDownLatch.await();
        return ("Time consumption: " + (System.currentTimeMillis() - startTime) + " ms");
        // return "ok";
    }

//    @PostMapping("/test/add")
//    public void add(@RequestParam int a, @RequestParam int b) {
//        Calculate calculate = JRpcProxy.getProxy(Calculate.class);
//        System.out.println(calculate.add(a, b));
//    }
}
