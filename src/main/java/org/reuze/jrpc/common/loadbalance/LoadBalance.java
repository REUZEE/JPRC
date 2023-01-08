package org.reuze.jrpc.common.loadbalance;

import org.reuze.jrpc.common.URL;
import org.reuze.jrpc.protocol.RpcRequest;

import java.util.List;

/**
 * @author Reuze
 * @Date 08/01/2023
 */
public interface LoadBalance {

    URL select(List<URL> urls);

    URL select(List<URL> urls, RpcRequest rpcRequest);
}
