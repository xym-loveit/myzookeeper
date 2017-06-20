package com.xym.zookeeper.zkapi
        ;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 删除znode节点
 *
 * @author xym
 */
public class Test05 implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) {

        ZooKeeper zooKeeper = null;
        try {
            zooKeeper = new ZooKeeper("192.168.2.135:2181", 5000, new Test05());

            connectedSemaphore.await();

            zooKeeper.create("/test-node", "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

            zooKeeper.delete("/test-node", Version.getRevision(), new IVoidVallBack(), "im contxt");


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
            connectedSemaphore.countDown();
        }
    }
}

class IVoidVallBack implements AsyncCallback.VoidCallback {

    public void processResult(int rc, String path, Object ctx) {
        System.out.println("删除节点结果：[" + rc + "," + path + "," + ctx + "]");
    }
}