package org.reuze.jrpc.registry;

import org.junit.Before;
import org.junit.Test;

import org.reuze.jrpc.common.URL;
import org.reuze.jrpc.registry.zk.RegistryFactory;
import org.reuze.jrpc.registry.zk.ZkRegistryFactory;
import org.reuze.jrpc.service.*;
import org.reuze.jrpc.service.impl.CalculateImpl;

/**
 * @author Reuze
 * @Date 08/01/2023
 */
public class ZkRegistryTest {

    private Registry registry;
    private String ip = "127.0.0.1";
    private int port = 2181;

    @Before
    public void connectTest() {
        RegistryFactory registryFactory = new ZkRegistryFactory();
        URL url = new URL();
        url.setIp("127.0.0.1");
        url.setPort(2181);
        registry = registryFactory.getRegistry(url);
    }

    @Test
    public void getChildrenTest() {
        URL url = new URL();
        url.setServiceName(Calculate.class.getName());
        System.out.println(registry.lookup(url));
    }

    @Test
    public void registerTest() {
        URL url = new URL();
        url.setPort(7777);
        url.setIp("127.0.0.1");
        Calculate calculate = new CalculateImpl();
        for (Class<?> service : calculate.getClass().getInterfaces()) {
            url.setServiceName(service.getName());
            registry.register(url);
        }
    }

    @Test
    public void unRegisterTest() {
        URL url = new URL();
        //url.setIp(ip);
        //url.setPort(8080);
        Calculate calculate = new CalculateImpl();
        for (Class<?> service : calculate.getClass().getInterfaces()) {
            url.setServiceName(service.getName());
            registry.unregister(url);
        }
    }
}
