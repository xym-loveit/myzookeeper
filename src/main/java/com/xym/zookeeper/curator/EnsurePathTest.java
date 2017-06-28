package com.xym.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * desc
 *
 * @author xym
 */
public class EnsurePathTest {

    private String path = "/zkpath";
    private static CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectionTimeoutMs(3000).sessionTimeoutMs(5000).connectString("192.168.2.135:2181").retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();

    public static void main(String[] args) {
        curatorFramework.start();

        curatorFramework.usingNamespace("zk-java");







    }

}