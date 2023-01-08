package org.reuze.jrpc.registry.zk;

import org.reuze.jrpc.common.URL;
import org.reuze.jrpc.registry.Registry;

/**
 * @author Reuze
 * @Date 08/01/2023
 */
public interface RegistryFactory {

    Registry getRegistry(URL url);
}
