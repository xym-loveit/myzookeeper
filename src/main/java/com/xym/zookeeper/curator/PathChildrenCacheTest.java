package com.xym.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.TimeUnit;

/**
 * 使用curator监控子节点变动
 *
 * @author xym
 */
public class PathChildrenCacheTest {

    static String path = "/node1/childrenCache";

    static CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString("192.168.2.135:2181").connectionTimeoutMs(3000).sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();

    public static void main(String[] args) {
        curatorFramework.start();
        PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework, path, true);
        try {
            pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
            pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
                public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                    switch (event.getType()) {
                        case CHILD_ADDED:
                            System.out.println("CHILD_ADDED, " + event.getData().getPath());
                        case CHILD_REMOVED:
                            System.out.println("CHILD_REMOVED, " + event.getData().getPath());
                        case CHILD_UPDATED:
                            System.out.println("CHILD_UPDATED, " + event.getData().getPath());
                        default:
                            break;
                    }
                }
            });

            curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
            TimeUnit.SECONDS.sleep(1);
            curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath(path + "/c1");
            TimeUnit.SECONDS.sleep(1);
            curatorFramework.delete().forPath(path + "/c1");
            TimeUnit.SECONDS.sleep(1);
            curatorFramework.delete().forPath(path);
            Thread.sleep(Integer.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}