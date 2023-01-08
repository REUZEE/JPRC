package org.reuze.jrpc.registry.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.reuze.jrpc.common.URL;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Reuze
 * @Date 08/01/2023
 */
public class CuratorZkClient {

    private final CuratorFramework client;

    public CuratorZkClient(URL url) {

        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(url.getAddress())
                .retryPolicy(new RetryNTimes(ZkClientOptions.RETRY_TIMES, ZkClientOptions.RETRY_SLEEP_MS))
                .connectionTimeoutMs(ZkClientOptions.DEFAULT_CONNECTION_TIMEOUT_MS)
                .sessionTimeoutMs(ZkClientOptions.DEFAULT_SESSION_TIMEOUT_MS);
        client = builder.build();
        client.start();
        try {
            client.blockUntilConnected(ZkClientOptions.DEFAULT_CONNECTION_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createNode(String path, CreateMode createMode) {
        try {
            client.create().creatingParentsIfNeeded().withMode(createMode).forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createEphemeralNode(String path) {
        createNode(path, CreateMode.EPHEMERAL);
    }

    public void createPersistentNode(String path) {
        createNode(path, CreateMode.PERSISTENT);
    }

    public void removeNode(String path) {
        try {
            client.delete().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getChildren(String path) {
        try {
            return client.getChildren().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public void addListener(String path, CuratorCacheListener listener) {
        CuratorCache curatorCache = CuratorCache.build(client, path);
        curatorCache.listenable().addListener(listener);
        curatorCache.start();
    }

}
