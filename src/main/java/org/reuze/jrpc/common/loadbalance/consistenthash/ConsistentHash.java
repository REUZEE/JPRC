package org.reuze.jrpc.common.loadbalance.consistenthash;

import org.reuze.jrpc.common.URL;
import org.reuze.jrpc.common.loadbalance.LoadBalance;

import java.util.*;

/**
 * @author Reuze
 * @Date 10/01/2023
 */
public class ConsistentHash<T> {

    private final HashFunction<Object> hashFunction;
    private final int numberOfReplicas;
    private final SortedMap<Integer, T> circle;

    public ConsistentHash(HashFunction<Object> hashFunction, int numberOfReplicas, Collection<T> nodes) {
        this.hashFunction = hashFunction;
        this.circle = new TreeMap<>();
        this.numberOfReplicas = numberOfReplicas;
        nodes.forEach(this::add);
    }

    public void add(T node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            circle.put(hashFunction.hash(node, i), node);
        }
    }

    public void remove(T node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            circle.remove(hashFunction.hash(node, i));
        }
    }

    public T get(Object key) {
        if (circle.isEmpty()) {
            return null;
        }
        int hash = hashFunction.hash(key);
        if (!circle.containsKey(hash)) {
            SortedMap<Integer, T> tailMap = circle.tailMap(hash);
            hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
        }
        return circle.get(hash);
    }

}
