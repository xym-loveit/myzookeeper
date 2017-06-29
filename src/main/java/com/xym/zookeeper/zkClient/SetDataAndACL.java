package com.xym.zookeeper.zkClient;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * desc
 *
 * @author xym
 */
public class SetDataAndACL implements Watcher {

    private static ZooKeeper zooKeeper;

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException, NoSuchAlgorithmException, KeeperException {
        zooKeeper = new ZooKeeper("192.168.2.135:2181", 5000, new SetDataAndACL());
        countDownLatch.await();

        //权限=(read/WRITE/CREATE/DELETE/ADMIN)权限+(scheme)模式+(id/digest)授权对象(192.168.2.102/user:password)
        List<ACL> acls = new ArrayList<ACL>();
        ACL aclIp = new ACL(ZooDefs.Perms.READ, new Id("ip", "192.168.2.102"));
        ACL aclDigest = new ACL(ZooDefs.Perms.READ | ZooDefs.Perms.WRITE, new Id("digest", DigestAuthenticationProvider.generateDigest("xym:123456")));
        acls.add(aclDigest);
        acls.add(aclIp);

        //String path = zooKeeper.create("/zk-study", "123".getBytes(), acls, CreateMode.PERSISTENT);
        String path = zooKeeper.create("/zk-study", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(path);

        Thread.sleep(Integer.MAX_VALUE);
    }

    public void process(WatchedEvent event) {
        if (event.getState() == Event.KeeperState.SyncConnected) {
            if (event.getType() == Event.EventType.None && event.getPath() == null) {
                countDownLatch.countDown();
            }
        }
    }
}