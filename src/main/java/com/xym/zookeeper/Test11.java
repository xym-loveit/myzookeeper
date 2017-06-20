package com.xym.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 异步更新节点内容
 *
 * @author xym
 */
public class Test11 implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zooKeeper = null;

    public static void main(String[] args) {
        try {
            String path = "/zk-book6";
            zooKeeper = new ZooKeeper("192.168.2.135:2181", 5000, new Test11());
            connectedSemaphore.await();

            zooKeeper.create(path, "test1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

            zooKeeper.getData(path, true, null);

            zooKeeper.setData(path, "test2".getBytes(), -1, new IStatCallback(), null);

            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    public void process(WatchedEvent event) {
        if (event.getState() == Event.KeeperState.SyncConnected) {
            if (event.getType() == Event.EventType.None && event.getPath() == null) {
                connectedSemaphore.countDown();
            }
        }
    }
}

class IStatCallback implements AsyncCallback.StatCallback {
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        System.out.println("rc=" + rc + ",path=" + path + ",ctx=" + ctx + ",stat=" + stat);
    }
}