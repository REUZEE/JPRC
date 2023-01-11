package org.reuze.jrpc;

import org.reuze.jrpc.server.RpcServer;
import org.reuze.jrpc.service.impl.CalculateImpl;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Reuze
 * @Date 11/01/2023
 */
@SpringBootApplication
public class Server2 {

    public static void main(String[] args) {
        RpcServer rpcServer;
        String ip = "127.0.0.1";
        int port = 7777;
        rpcServer = new RpcServer(ip, port);
        rpcServer.start();
        rpcServer.register(new CalculateImpl());
    }
}
