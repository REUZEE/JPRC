package org.reuze.jrpc.registry.zk;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Reuze
 * @Date 08/01/2023
 */

public class ZkOptions {

    /**
     * 默认连接超时毫秒数
     */
    public static final int DEFAULT_CONNECTION_TIMEOUT_MS = 5000;
    /**
     * 默认 session 超时毫秒数
     */
    public static final int DEFAULT_SESSION_TIMEOUT_MS = 60000;
    /**
     * session 超时时间
     */
    public static final String SESSION_TIMEOUT_KEY = "zk.sessionTimeoutMs";
    /**
     * 连接重试次数
     */
    public static final int RETRY_TIMES = 3;
    /**
     * 连接重试睡眠毫秒数
     */
    public static final int RETRY_SLEEP_MS = 1000;
    /**
     * 根目录
     */
    public static final String ROOT_PATH = "/rpc";

    public static final String ZK_IP = "127.0.0.1";

    public static final int ZK_PORT = 2181;

}
