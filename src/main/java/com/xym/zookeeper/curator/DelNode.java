package com.xym.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * 使用Curator删除节点
 *
 * @author xym
 */
public class DelNode {

    static String path = "/node1/c1";
    static CuratorFramework client = CuratorFrameworkFactory.builder().connectString("192.168.2.135:2181").sessionTimeoutMs(5000).connectionTimeoutMs(3000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();

    public static void main(String[] args) {
        client.start();
        try {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, "init".getBytes());
            Stat stat = new Stat();
            byte[] data=client.getData().storingStatIn(stat).forPath(path);
            System.out.println(new String(data));
            client.delete().deletingChildrenIfNeeded().withVersion(stat.getVersion()).forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        client.close();
    }

}