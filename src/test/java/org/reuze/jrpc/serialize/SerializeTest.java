package org.reuze.jrpc.serialize;

import com.esotericsoftware.kryo.Kryo;
import org.junit.Test;
import org.reuze.jrpc.protocol.RpcRequest;
import org.reuze.jrpc.protocol.serialize.KryoSerializer;
import org.reuze.jrpc.protocol.serialize.Serializer;
import org.reuze.jrpc.protocol.serialize.SerializerType;
import org.reuze.jrpc.service.Calculate;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author Reuze
 * @Date 13/01/2023
 */
public class SerializeTest {

    Serializer serializer = new KryoSerializer();

    @Test
    public void serialize() throws NoSuchMethodException {
        RpcRequest rpcRequest = new RpcRequest();
        Method method = Calculate.class.getMethod("add", int.class, int.class);
        rpcRequest.setSerializeType(SerializerType.KRYO_SERIALIZER.getValue());
        rpcRequest.setRequestId(UUID.randomUUID().toString());
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameterTypes(method.getParameterTypes());
        rpcRequest.setParameter(new Object[]{31, 13});

        System.out.println(method.getName());

        byte[] bytes = serializer.serialize(rpcRequest);

        serializer.deSerialize(bytes, RpcRequest.class);
        System.out.println();


    }

}
