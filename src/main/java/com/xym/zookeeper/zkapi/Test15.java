package com.xym.zookeeper.zkapi
        ;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * 使用错误的权限信息会话访问含权限信息的znode(KeeperException$NoAuthException)
 *
 * @author xym
 */
public class Test15 {
    public static void main(String[] args) {
        try {
            ZooKeeper zooKeeper = new ZooKeeper("192.168.2.135:2181", 5000, null);

            zooKeeper.addAuthInfo("digest", "xym:123".getBytes());
            zooKeeper.create("/zk-authNode", "authinfo".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);


            ZooKeeper zooKeeper2 = new ZooKeeper("192.168.2.135:2181", 5000, null);
            zooKeeper2.addAuthInfo("digest", "xym:123".getBytes());
            byte[] bytes = zooKeeper2.getData("/zk-authNode", false, null);
            System.out.println(new String(bytes));

            ZooKeeper zooKeeper3 = new ZooKeeper("192.168.2.135:2181", 5000, null);
            //错误的权限
            zooKeeper3.addAuthInfo("digest", "xym:1234".getBytes());
            zooKeeper3.getData("/zk-authNode", false, null);


            Thread.sleep(Integer.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }


    }
}