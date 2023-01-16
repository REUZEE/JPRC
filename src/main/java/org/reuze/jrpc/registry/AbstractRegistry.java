package org.reuze.jrpc.registry;

import lombok.extern.slf4j.Slf4j;
import org.reuze.jrpc.common.URL;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

/**
 * @author Reuze
 * @Date 08/01/2023
 */
@Slf4j
public abstract class AbstractRegistry implements Registry {

    private final Map<String, Set<URL>> registered = new ConcurrentHashMap<>();

    protected abstract void doRegister(URL url);

    protected abstract void doUnregister(URL url);

    protected abstract List<URL> doLookup(URL condition);

    @Override
    public void register(URL url) {
        doRegister(url);
        addToLocalCache(url);
        log.info("Register: {}", url);
    }

    @Override
    public void unregister(URL url) {
        doUnregister(url);
        removeFromLocalCache(url);
        log.info("Unregister: {}", url);
    }

    @Override
    public List<URL> lookup(URL condition) {
        String serviceName = condition.getServiceName();
        if (registered.containsKey(serviceName) || registered.get(serviceName) != null) {
            return new ArrayList<>(registered.get(serviceName));
        }
        List<URL> urls = reset(condition);
        return urls;
    }

    public List<URL> reset(URL condition) {
        String serviceName = condition.getServiceName();
        registered.remove(serviceName);
        List<URL> urls = doLookup(condition);
        for (URL url : urls) {
            addToLocalCache(url);
        }
        return urls;
    }

    private void addToLocalCache(URL url) {
        String serviceName = url.getServiceName();
        if (!registered.containsKey(serviceName)) {
            registered.put(serviceName, new HashSet<>());
        }
        registered.get(serviceName).add(url);
    }

    private void removeFromLocalCache(URL url) {
        String serviceName = url.getServiceName();
        if (registered.containsKey(serviceName)) {
            registered.get(serviceName).remove(url);
        }
    }
}
