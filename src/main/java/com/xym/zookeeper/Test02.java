package com.xym.zookeeper
        ;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 复用session 和password
 *
 * @author xym
 */
public class Test02 implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) {
        try {
            ZooKeeper zooKeeper = new ZooKeeper("192.168.2.135:2181", 5000, new Test02());
            connectedSemaphore.await();
            long sessionId = zooKeeper.getSessionId();
            byte[] sessionPasswd = zooKeeper.getSessionPasswd();

            System.out.println("sessionid=" + sessionId + "\n" + "sessionPasswd=" + new String(sessionPasswd));

            //尝试使用错误的sid
            zooKeeper = new ZooKeeper("192.168.2.135:2181", 5000, new Test02(), 11L, "test".getBytes());

//使用正确的
            zooKeeper = new ZooKeeper("192.168.2.135:2181", 5000, new Test02(), sessionId, sessionPasswd);


            Thread.sleep(Integer.MAX_VALUE);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public void process(WatchedEvent watchedEvent) {

        System.out.println("watchedEvent ：" + watchedEvent);
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            connectedSemaphore.countDown();
        }

    }
}