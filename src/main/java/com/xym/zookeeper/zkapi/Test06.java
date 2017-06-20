package com.xym.zookeeper.zkapi
        ;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 同步获取子节点列表
 *
 * @author xym
 */
public class Test06 implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zooKeeper = null;

    public static void main(String[] args) {
        try {
            String path = "/zk-book";
            zooKeeper = new ZooKeeper("192.168.2.135:2181", 5000, new Test06());
            connectedSemaphore.await();
            zooKeeper.create(path, "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            zooKeeper.create(path + "/c1", "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

            System.out.println(zooKeeper.getChildren(path, true));

            zooKeeper.create(path + "/c2", "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

            TimeUnit.SECONDS.sleep(1);

            zooKeeper.create(path + "/c3", "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

            Thread.sleep(Integer.MAX_VALUE);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }

    }

    public void process(WatchedEvent event) {
        if (event.getState() == Event.KeeperState.SyncConnected) {
            if (event.getType() == Event.EventType.None && event.getPath() == null) {
                connectedSemaphore.countDown();
            } else if (event.getType() == Event.EventType.NodeChildrenChanged) {
                System.out.println("eventPath=" + event.getPath());
                try {
                    List<String> children = zooKeeper.getChildren(event.getPath(), true);
                    System.out.println("新子节点列表：" + children);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}