package org.reuze.jrpc.registry;

import lombok.extern.slf4j.Slf4j;
import org.reuze.jrpc.common.URL;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Reuze
 * @Date 08/01/2023
 */
@Slf4j
public abstract class AbstractRegistry implements Registry {

    private final Map<String, Set<String>> registered = new ConcurrentHashMap<>();

    protected abstract void doRegister(URL url);

    protected abstract void doUnregister(URL url);

    protected abstract List<URL> doLookup(URL condition);

    @Override
    public void register(URL url) {
        doRegister(url);
        log.info("Register: {}", url);
    }

    @Override
    public void unregister(URL url) {
        doUnregister(url);
        log.info("Unregister: {}", url);
    }

    @Override
    public List<URL> lookup(URL condition) {
        // String serviceName = condition.getServiceName();
        return doLookup(condition);
    }
}
