package org.reuze.jrpc;

import org.junit.After;
import org.junit.Test;
import org.reuze.jrpc.client.RpcClient;
import org.reuze.jrpc.common.JRpcProxy;
import org.reuze.jrpc.service.Calculate;
import org.reuze.jrpc.service.impl.CalculateImpl;

/**
 * @author Reuze
 * @Date 05/01/2023
 */
public class Client {

    RpcClient rpcClient;

    @Test
    public void rpc() {
        String ip = "127.0.0.1";
        int port = 8080;
        int a = 3;
        int b = 6;
        rpcClient = new RpcClient(ip, port);
        Calculate calculate = JRpcProxy.getProxy(rpcClient, Calculate.class);
        System.out.println(calculate.add(a, b));
    }

    @After
    public void end() {
        rpcClient.stop();
    }
}
