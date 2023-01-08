package org.reuze.jrpc;

import org.junit.After;
import org.junit.Test;
import org.reuze.jrpc.server.RpcServer;
import org.reuze.jrpc.service.impl.CalculateImpl;

/**
 * @author Reuze
 * @Date 05/01/2023
 */
public class Server {

    RpcServer rpcServer;

    @Test
    public void rpc() throws InterruptedException {
        String ip = "127.0.0.1";
        int port = 8080;
        rpcServer = new RpcServer(ip, port);
        rpcServer.start();
        rpcServer.register(new CalculateImpl());
        Thread.sleep(100000000);
    }

    @After
    public void end() {
        rpcServer.stop();
    }
}
