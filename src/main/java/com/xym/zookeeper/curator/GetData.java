package com.xym.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * 使用curator读取内容
 *
 * @author xym
 */
public class GetData {

    static String path = "/node1/c1";
    static CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString("192.168.2.135:2181").connectionTimeoutMs(5000).sessionTimeoutMs(8000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();

    public static void main(String[] args) {
        curatorFramework.start();
        try {
            curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, "init".getBytes());
            Stat stat = new Stat();
            System.out.println(new String(curatorFramework.getData().storingStatIn(stat).forPath(path)));
            System.out.printf("stat=" + stat);
        } catch (Exception e) {
            e.printStackTrace();
        }

        curatorFramework.close();
    }

}