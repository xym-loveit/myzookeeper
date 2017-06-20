package com.xym.zookeeper.zkapi
        ;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 同步创建znode临时节点
 *
 * @author xym
 */
public class Test03 implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) {

        try {
            ZooKeeper zooKeeper = new ZooKeeper("192.168.2.135:2181", 5000, new Test03());

            connectedSemaphore.await();

            String path = zooKeeper.create("/test-ephemeral-", "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

            System.out.println("path=" + path);

            String path2 = zooKeeper.create("/test-ephemeral-", "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

            System.out.println("path=" + path2);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }

    }


    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            connectedSemaphore.countDown();
        }
    }
}