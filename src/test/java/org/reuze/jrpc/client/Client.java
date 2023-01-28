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

import java.util.Random;
import java.util.concurrent.*;

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
    public void rpc1() throws InterruptedException {
        int a = 3;
        int b = 6;
        ExecutorService executorService = new ThreadPoolExecutor(4,
                10000, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
        /* ------------------------------------------------------
         *   1  |  5   |  10  |  50  | 100  | 500 |
         * ------------------------------------------------------
         * 2139 | 2157 | 2334 | 2644 | 2824 |
         * -----------------------------------------------------*/
        int COUNT = 100;
        CountDownLatch countDownLatch = new CountDownLatch(COUNT);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            executorService.submit(new AddTask(countDownLatch));
        }
        countDownLatch.await();
        System.out.println("Time consumption: " + (System.currentTimeMillis() - startTime) + " ms");

    }

    class AddTask implements Runnable {

        CountDownLatch countDownLatch;

        public AddTask(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            Random random = new Random();
            Calculate calculate = JRpcProxy.getProxy(Calculate.class);
            System.out.println(calculate.add(random.nextInt(1000), random.nextInt(1000)));
            countDownLatch.countDown();
        }
    }



    @After
    public void end() {
        if (rpcClient != null) {
            rpcClient.stop();
        }
    }
}
