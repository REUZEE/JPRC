package org.reuze.jrpc.registry.zk;

import org.reuze.jrpc.common.URL;
import org.reuze.jrpc.registry.Registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Reuze
 * @Date 08/01/2023
 */
public class ZkRegistryFactory implements RegistryFactory {

    private static final Map<URL, ZkRegistry> cache = new ConcurrentHashMap<>();

    @Override
    public Registry getRegistry(URL url) {
        if (cache.containsKey(url)) {
            return cache.get(url);
        }
        ZkRegistry zkRegistry = new ZkRegistry(url);
        cache.putIfAbsent(url, zkRegistry);
        return cache.get(url);
    }
}
