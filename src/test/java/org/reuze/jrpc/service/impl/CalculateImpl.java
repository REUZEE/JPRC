package org.reuze.jrpc.service.impl;

import org.reuze.jrpc.service.Calculate;

/**
 * @author Reuze
 * @Date 05/01/2023
 */
public class CalculateImpl implements Calculate {

    public CalculateImpl(){}

    public int add(int a, int b) {
        return a + b;
    }
}
