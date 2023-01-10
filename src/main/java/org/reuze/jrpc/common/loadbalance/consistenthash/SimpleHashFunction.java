package org.reuze.jrpc.common.loadbalance.consistenthash;

/**
 * @author Reuze
 * @Date 10/01/2023
 */
public class SimpleHashFunction<T> implements HashFunction<T> {

    @Override
    public int hash(T t) {
        int h;
        return (t == null) ? 0 : (h = t.hashCode()) ^ (h >>> 16);
    }

    @Override
    public int hash(T t, int i) {
        return 0;
    }
}
