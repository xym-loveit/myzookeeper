package com.xym.zookeeper.zkapi
        ;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * 使用含权限信息的会话创建znode
 *
 * @author xym
 */
public class Test13 {

    public static void main(String[] args) {

        try {
            ZooKeeper zooKeeper = new ZooKeeper("192.168.2.135:2181", 5000, null);
            zooKeeper.addAuthInfo("digest", "xym:123".getBytes());
            zooKeeper.create("/zk-authNode", "authinfo".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);

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