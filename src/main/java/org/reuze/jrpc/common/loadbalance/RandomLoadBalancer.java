package org.reuze.jrpc.common.loadbalance;

import org.reuze.jrpc.common.URL;
import org.reuze.jrpc.protocol.RpcRequest;

import java.util.List;
import java.util.Random;

/**
 * @author Reuze
 * @Date 08/01/2023
 */
public class RandomLoadBalancer implements LoadBalance {

    @Override
    public URL select(List<URL> urls) {
        int size = urls.size();
        Random random = new Random();
        int idx = random.nextInt(size);
        return urls.get(idx);
    }
}
