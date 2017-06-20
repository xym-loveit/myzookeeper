package com.xym.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * 删除节点的权限控制特殊性
 * 如果有子节点，则删除子节点需要提供权限验证
 *
 * @author xym
 */
public class Test16 {
    public static void main(String[] args) {
        try {
            ZooKeeper zooKeeper = new ZooKeeper("192.168.2.135:2181", 5000, null);
            zooKeeper.addAuthInfo("digest", "xym:123".getBytes());
            zooKeeper.create("/zk-auth-test", "test".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);


            ZooKeeper zooKeeper2 = new ZooKeeper("192.168.2.135:2181", 5000, null);
            zooKeeper2.delete("/zk-auth-test", -1);
            System.out.println("删除节点不需要权限验证？");

            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void delNode1() {
        String path = "/zk-auth-test";
        String pathC = path + "/c1";
        try {
            ZooKeeper zooKeeper = new ZooKeeper("192.168.2.135:2181", 5000, null);
            zooKeeper.addAuthInfo("digest", "xym:123".getBytes());
            zooKeeper.create(path, "test".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
            //创建子节点
            zooKeeper.create(pathC, "test".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }

        try {
            //没有设置权限信息删除子节点
            ZooKeeper zooKeeper2 = new ZooKeeper("192.168.2.135:2181", 5000, null);
            zooKeeper2.delete(pathC, -1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }


        try {
            //设置权限信息删除子节点
            ZooKeeper zooKeeper3 = new ZooKeeper("192.168.2.135:2181", 5000, null);
            zooKeeper3.addAuthInfo("digest", "xym:123".getBytes());
            zooKeeper3.delete(pathC, -1);
            System.out.println("成功删除子节点" + pathC);
            //没有设置权限信息删除父节点
            ZooKeeper zooKeeper4 = new ZooKeeper("192.168.2.135:2181", 5000, null);
            zooKeeper4.delete(path, -1);
            System.out.println("成功删除父节点" + path);

            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}