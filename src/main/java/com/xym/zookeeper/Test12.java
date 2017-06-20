package com.xym.zookeeper
        ;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 同步检测节点会否存在
 *
 * @author xym
 */
public class Test12 implements Watcher {
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zooKeeper = null;

    public static void main(String[] args) {
        String path = "/zk-book7";
        try {
            zooKeeper = new ZooKeeper("192.168.2.135:2181", 5000, new Test12());
            connectedSemaphore.await();

            //清理工作
            Stat stat = zooKeeper.exists(path, true);
            if (stat != null) {
                List<String> children = zooKeeper.getChildren(path, null);
                for (String child : children) {
                    //System.out.println(child);
                    //相对路径需要转换为绝对路径
                    zooKeeper.delete(path + "/" + child, -1);
                }
                zooKeeper.delete(path, -1);
            }

            //create
            zooKeeper.create(path, "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            //update
            zooKeeper.setData(path, "test1".getBytes(), -1);
            //create
            zooKeeper.create(path + "/c1", "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            //删除子节点并不会产生删除事件
            zooKeeper.delete(path + "/c1", -1);
            zooKeeper.delete(path, -1);

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
        try {
            if (event.getState() == Event.KeeperState.SyncConnected) {
                if (event.getType() == Event.EventType.None && event.getPath() == null) {
                    connectedSemaphore.countDown();
                } else if (event.getType() == Event.EventType.NodeDataChanged) {
                    System.out.println("Node[" + event.getPath() + "]NodeDataChanged");
                    zooKeeper.exists(event.getPath(), true);
                } else if (event.getType() == Event.EventType.NodeCreated) {
                    System.out.println("Node[" + event.getPath() + "]NodeCreated");
                    zooKeeper.exists(event.getPath(), true);
                } else if (event.getType() == Event.EventType.NodeDeleted) {
                    System.out.println("Node[" + event.getPath() + "]NodeDeleted");
                    zooKeeper.exists(event.getPath(), true);
                }
            }
        } catch (Exception e) {
        }
    }
}