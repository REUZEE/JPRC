package org.reuze.jrpc.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Reuze
 * @Date 08/01/2023
 */
@Getter
@Setter
@ToString
public class URL {

    String ip;
    int port;
    String serviceName;

    public String getAddress() {
        if (ip == null || ip.equals("")) {
            return null;
        }
        return ip + ":" + port;
    }
}
