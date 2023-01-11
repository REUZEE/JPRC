package org.reuze.jrpc.common.loadbalance.consistenthash;

/**
 * @author Reuze
 * @Date 10/01/2023
 */
public interface HashFunction<T> {

    int hash(T t);

    int hash(T t, int i);
}
