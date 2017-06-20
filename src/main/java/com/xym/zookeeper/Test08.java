package com.xym.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 同步获取节点内容
 *
 * @author xym
 */
public class Test08 implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zooKeeper = null;
    private static Stat stat = new Stat();

    public static void main(String[] args) {
        String path = "/zk-book4";
        try {
            zooKeeper = new ZooKeeper("192.168.2.135:2181", 5000, new Test08());
            connectedSemaphore.await();

            zooKeeper.create(path, "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

            System.out.println(new String(zooKeeper.getData(path, true, stat)));

            System.out.println(stat.getCzxid() + "," + stat.getMzxid() + "," + stat.getVersion());

            zooKeeper.setData(path, "test6".getBytes(), -1);

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
            } else if (event.getType() == Event.EventType.NodeDataChanged) {
                try {
                    System.out.println(new String(zooKeeper.getData(event.getPath(), true, stat)));
                    System.out.println("-------------------------");
                    System.out.println(stat.getCzxid() + "," + stat.getMzxid() + "," + stat.getVersion());
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}