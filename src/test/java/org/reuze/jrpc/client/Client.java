package org.reuze.jrpc.client;

import org.junit.After;
import org.junit.Test;

import org.reuze.jrpc.client.RpcClient;
import org.reuze.jrpc.common.proxy.JRpcProxy;
import org.reuze.jrpc.service.Calculate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;

/**
 * @author Reuze
 * @Date 05/01/2023
 */
@SpringBootApplication
public class Client implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

    public static void main(String[] args) {
        SpringApplication.run(Client.class, args);
    }

    @Override
    public void customize(ConfigurableServletWebServerFactory factory) {
        factory.setPort(18080);
    }

    RpcClient rpcClient;

    @Test
    public void rpc() {
        String ip = "127.0.0.1";
        int port = 6666;
        int a = 3;
        int b = 6;
        rpcClient = new RpcClient(ip, port);
        Calculate calculate = JRpcProxy.getProxy(rpcClient, Calculate.class);
        System.out.println(calculate.add(a, b));
    }

    @Test
    public void rpc1() {
        int a = 3;
        int b = 6;
        Calculate calculate = JRpcProxy.getProxy(Calculate.class);
        System.out.println(calculate.add(a, b));
    }

    @After
    public void end() {
        if (rpcClient != null) {
            rpcClient.stop();
        }
    }
}
