package org.reuze.jrpc;

import org.junit.Test;
import org.reuze.jrpc.protocol.RpcRequest;
import org.reuze.jrpc.service.impl.CalculateImpl;

import java.lang.reflect.Method;

/**
 * @author Reuze
 * @Date 05/01/2023
 */
public class ReflectTest {

    @Test
    public void reflectTest() throws Exception {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName("org.reuze.jrpc.service.impl.CalculateImpl");
        rpcRequest.setMethodName("add");
        rpcRequest.setParameterTypes(new Class<?>[]{int.class, int.class});
        rpcRequest.setParameter(new Object[]{2, 3});

        Class<?> clz = Class.forName(rpcRequest.getClassName());
        System.out.println(clz);
        Object object = clz.newInstance();
        Method method = clz.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
        System.out.println(method.invoke(object, rpcRequest.getParameter()));
    }

    @Test
    public void classNameTest() throws NoSuchMethodException {
        Method method = CalculateImpl.class.getMethod("add", int.class, int.class);
        System.out.println(method.getDeclaringClass());
    }
}
