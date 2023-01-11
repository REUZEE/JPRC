package org.reuze.jrpc.client.controller;

import org.reuze.jrpc.common.proxy.JRpcProxy;
import org.reuze.jrpc.service.Calculate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Reuze
 * @Date 11/01/2023
 */
@Controller
public class TestController {

    @GetMapping("/test/add")
    public void add(@RequestParam int a, @RequestParam int b) {
        Calculate calculate = JRpcProxy.getProxy(Calculate.class);
        System.out.println(calculate.add(a, b));
    }

//    @PostMapping("/test/add")
//    public void add(@RequestParam int a, @RequestParam int b) {
//        Calculate calculate = JRpcProxy.getProxy(Calculate.class);
//        System.out.println(calculate.add(a, b));
//    }
}
