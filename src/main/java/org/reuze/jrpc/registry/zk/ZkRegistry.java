package org.reuze.jrpc.registry.zk;

import org.reuze.jrpc.common.URL;
import org.reuze.jrpc.registry.AbstractRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Reuze
 * @Date 08/01/2023
 */
public class ZkRegistry extends AbstractRegistry {

    private final CuratorZkClient zkClient;

    private static final String ROOT_PATH = "/rpc";

    public static final String ZK_IP = "127.0.0.1";

    public static final int ZK_PORT = 2181;

    public ZkRegistry() {
        URL url = new URL();
        url.setIp(ZK_IP);
        url.setPort(ZK_PORT);
        zkClient = new CuratorZkClient(url);
    }

    public ZkRegistry(URL url) {
        zkClient = new CuratorZkClient(url);
    }

    @Override
    protected void doRegister(URL url) {
        zkClient.createEphemeralNode(url2Path(url));
    }

    @Override
    protected void doUnregister(URL url) {
        zkClient.removeNode(url2Path(url));
    }

    @Override
    protected List<URL> doLookup(URL condition) {
        List<String> children = zkClient.getChildren(url2Path(condition));
        List<URL> urls = new ArrayList<>(children.size());
        for (String child : children) {
            URL url = new URL();
            url.setIp(child.split(":")[0]);
            url.setPort(Integer.parseInt(child.split(":")[1]));
            url.setServiceName(condition.getServiceName());
            urls.add(url);
        }
        return urls;
    }

    private void watch(URL url) {
        // String path =
    }

    private String url2Path(URL url) {
        if (url.getAddress() == null) {
            return ROOT_PATH + "/" + url.getServiceName();
        }
        return ROOT_PATH + "/" + url.getServiceName() + "/" + url.getAddress();
    }

}
