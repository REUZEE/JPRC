package org.reuze.jrpc.registry;

import org.reuze.jrpc.common.URL;

import java.util.List;

/**
 * @author Reuze
 * @Date 08/01/2023
 */
public interface Registry {

    void register(URL url);

    void unregister(URL url);

    List<URL> lookup(URL condition);
}
