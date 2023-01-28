package org.reuze.jrpc.registry.zk;

import lombok.extern.slf4j.Slf4j;
import org.reuze.jrpc.common.URL;
import org.reuze.jrpc.registry.AbstractRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Reuze
 * @Date 08/01/2023
 */
@Slf4j
public class ZkRegistry extends AbstractRegistry {

    private final CuratorZkClient zkClient;

    public ZkRegistry() {
        URL url = new URL();
        url.setIp(ZkOptions.ZK_IP);
        url.setPort(ZkOptions.ZK_PORT);
        zkClient = new CuratorZkClient(url);
    }

    public ZkRegistry(URL url) {
        zkClient = new CuratorZkClient(url);
    }

    @Override
    protected void doRegister(URL url) {
        zkClient.createEphemeralNode(url2Path(url));
        watch(url);
    }

    @Override
    protected void doUnregister(URL url) {
        zkClient.removeNode(url2Path(url));
        watch(url);
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
        for (URL url : urls) {
            watch(url);
        }
        return urls;
    }

    private void watch(URL url) {
        String path = url2Path(url);
        zkClient.addListener(path, ((type, oldData, data) -> {
            log.info("watch event, type = {}, oldData = {}, data = {}", type, oldData, data);
            reset(url);
        }));
    }

    private String url2Path(URL url) {
        if (url.getAddress() == null) {
            return ZkOptions.ROOT_PATH + "/" + url.getServiceName();
        }
        return ZkOptions.ROOT_PATH + "/" + url.getServiceName() + "/" + url.getAddress();
    }

}
