package com.xym.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.ZooKeeper;

/**
 * desc
 *
 * @author xym
 */
public class ZKPathTest {
    private static String path = "/curator_zkpath";

    private static CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString("192.168.2.135:2181").connectionTimeoutMs(5000).sessionTimeoutMs(3000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();


    public static void main(String[] args) {
        curatorFramework.start();
        try {
            ZooKeeper zooKeeper = curatorFramework.getZookeeperClient().getZooKeeper();
            System.out.println(ZKPaths.fixForNamespace(path, "/sub"));
            System.out.println(ZKPaths.makePath(path, "/sub"));
            System.out.println(ZKPaths.getNodeFromPath(path + "/sub1"));
            ZKPaths.PathAndNode pathAndNode = ZKPaths.getPathAndNode("/aaaa/sub1");
            System.out.println(pathAndNode.getNode());
            System.out.println(pathAndNode.getPath());
            ZKPaths.mkdirs(zooKeeper, path + "/child1");
            ZKPaths.mkdirs(zooKeeper, path + "/child2");
            //ZKPaths.deleteChildren(zooKeeper, path, false);
            System.out.println(ZKPaths.getSortedChildren(zooKeeper, path));
            ZKPaths.deleteChildren(curatorFramework.getZookeeperClient().getZooKeeper(), path, true);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}