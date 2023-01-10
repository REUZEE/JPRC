package org.reuze.jrpc.common.loadbalance;

import org.reuze.jrpc.common.URL;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Reuze
 * @Date 10/01/2023
 */
public class RoundRobinLoadBalancer implements LoadBalance {

    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public URL select(List<URL> urls) {
        int idx = counter.getAndIncrement() % urls.size();
        return urls.get(idx);
    }
}
