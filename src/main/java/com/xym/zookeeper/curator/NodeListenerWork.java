package com.xym.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.RetryUntilElapsed;

/**
 * desc
 *
 * @author xym
 */
public class NodeListenerWork {

    private static CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString("192.168.2.135:2181").connectionTimeoutMs(5000).sessionTimeoutMs(5000).retryPolicy(new RetryUntilElapsed(5000, 1000)).build();

    public static void main(String[] args) throws Exception {

        curatorFramework.start();

        final NodeCache nodeCache = new NodeCache(curatorFramework, "/zk-study");

        nodeCache.start();
        //默认监听创建和修改
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            public void nodeChanged() throws Exception {
                System.out.println(new String(nodeCache.getCurrentData().getData()));
                System.out.println(nodeCache.getCurrentData().getPath());
                System.out.println(nodeCache.getCurrentData().getStat());
            }
        });


        final PathChildrenCache childrenCache = new PathChildrenCache(curatorFramework, "/zk-study", true);
        childrenCache.start();

        childrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            //子节点添加、删除、修改都会触发
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                System.out.println(event.getData().getPath() + "," + event.getType());
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                for (ChildData childData : childrenCache.getCurrentData()) {
                    System.out.println(childData.getPath() + ":" + new String(childData.getData()));
                }
            }
        });


        Thread.sleep(Integer.MAX_VALUE);
    }

}