package com.xym.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.TimeUnit;

/**
 * 使用curator实现类watcher
 *
 * @author xym
 */
public class NodeCacheTest {
    static String path = "/node1/nodecache";
    static CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString("192.168.2.135:2181").connectionTimeoutMs(3000).sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();

    public static void main(String[] args) {
        curatorFramework.start();
        try {
            curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, "init".getBytes());

            final NodeCache nodeCache = new NodeCache(curatorFramework, path, false);
            nodeCache.start(true);
            nodeCache.getListenable().addListener(new NodeCacheListener() {
                public void nodeChanged() throws Exception {
                    System.out.println("Node data update,New data " + new String(nodeCache.getCurrentData().getData()));
                }
            });

            curatorFramework.setData().forPath(path, "update".getBytes());
            TimeUnit.SECONDS.sleep(1);
            curatorFramework.delete().forPath(path);
            Stat stat = curatorFramework.checkExists().forPath(path);
            System.out.println(stat);
            Thread.sleep(Integer.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}