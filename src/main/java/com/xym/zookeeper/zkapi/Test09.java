package com.xym.zookeeper.zkapi
        ;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 异步获取节点内容
 *
 * @author xym
 */
public class Test09 implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zooKeeper = null;
    private static Stat stat = new Stat();

    public static void main(String[] args) {
        try {
            String path = "/zk-book4";
            zooKeeper = new ZooKeeper("192.168.2.135:2181", 5000, new Test09());
            connectedSemaphore.await();

            zooKeeper.create(path, "test4".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

            zooKeeper.getData(path, true, new IDataCallBack(), null);

            zooKeeper.setData(path, "test5".getBytes(), -1);

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
            } else if (event.getType() == Event.EventType.NodeDataChanged) {
                zooKeeper.getData(event.getPath(), true, new IDataCallBack(), null);
            }
        }
    }
}

class IDataCallBack implements AsyncCallback.DataCallback {

    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
        System.out.println("--callback--");
        System.out.println("rc=" + rc + ",path=" + path + ",data=" + new String(data));
        System.out.println(stat.getCzxid() + "," + stat.getMzxid() + "," + stat.getVersion());
    }
}