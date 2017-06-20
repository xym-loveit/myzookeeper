package com.xym.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * 使用无权限信息的会话访问含权限信息的znode抛出异常（KeeperException$NoAuthException）
 *
 * @author xym
 */
public class Test14 {

    public static void main(String[] args) {

        try {
            ZooKeeper zooKeeper = new ZooKeeper("192.168.2.135:2181", 5000, null);

            zooKeeper.addAuthInfo("digest", "xym:123".getBytes());
            zooKeeper.create("/zk-authNode", "authinfo".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);
            ZooKeeper zooKeeper1 = new ZooKeeper("192.168.2.135:2181", 5000, null);
            //zooKeeper1.addAuthInfo("digest", "xym:123".getBytes());
            byte[] bytes = zooKeeper1.getData("/zk-authNode", false, null);
            //System.out.println(new String(bytes));
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