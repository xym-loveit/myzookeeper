package com.xym.zookeeper.zkapi;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 同步更新节点内容
 *
 * @author xym
 */
public class Test10 implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zooKeeper = null;

    public static void main(String[] args) {
        try {
            String path = "/zk-book5";
            zooKeeper = new ZooKeeper("192.168.2.135:2181", 5000, new Test10());
            connectedSemaphore.await();

            zooKeeper.create(path, "test5".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

            zooKeeper.getData(path, true, null);

            Stat stat = zooKeeper.setData(path, "test6".getBytes(), -1);
            System.out.println(stat.getCzxid() + "," + stat.getMzxid() + "," + stat.getVersion());
            Stat stat2 = zooKeeper.setData(path, "test7".getBytes(), stat.getVersion());
            System.out.println(stat2.getCzxid() + "," + stat2.getMzxid() + "," + stat2.getVersion());

            //无效版本更新失败
            zooKeeper.setData(path, "test8".getBytes(), stat.getVersion());


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