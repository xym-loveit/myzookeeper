package com.xym.zookeeper
        ;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 最基本zk会话实例
 *
 * @author xym
 */
public class Test01 implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) {

        ZooKeeper keeper = null;
        try {
            keeper = new ZooKeeper("192.168.2.135:2181", 5000, new Test01());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(keeper.getState());
        try {
            connectedSemaphore.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("会话创建完成！");
    }

    public void process(WatchedEvent watchedEvent) {
        System.out.println("watchedEvent ：" + watchedEvent);
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            connectedSemaphore.countDown();
        }
    }
}