package com.xym.zookeeper.zkapi;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * desc
 *
 * @author xym
 */
public class ConnectionWatcher implements Watcher {

    protected ZooKeeper zk;
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public void connect(String host) throws IOException, InterruptedException {
        zk = new ZooKeeper(host, 5000, this);
        countDownLatch.await();
    }

    public void process(WatchedEvent event) {
        if (event
                .getState() == Event.KeeperState.SyncConnected) {
            countDownLatch.countDown();
        }
    }

    public void close() throws InterruptedException {
        zk.close();
    }
}