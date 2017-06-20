package com.xym.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * 使用curator创建节点
 *
 * @author xym
 */
public class CreateNode {
    static String path = "/node1/c1";
    static CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectionTimeoutMs(5000).sessionTimeoutMs(5000).connectString("192.168.2.135:2181").retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();

    public static void main(String[] args) {
        curatorFramework.start();
        try {
            curatorFramework.delete().deletingChildrenIfNeeded().forPath(path);
            curatorFramework.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, "init".getBytes());
            curatorFramework.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}