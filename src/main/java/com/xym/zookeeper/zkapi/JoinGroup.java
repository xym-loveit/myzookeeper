package com.xym.zookeeper.zkapi;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;

import java.io.IOException;

/**
 * desc
 *
 * @author xym
 */
public class JoinGroup extends ConnectionWatcher {

    public void join(String group, String member) throws KeeperException, InterruptedException {
        String path = zk.create("/" + group + "/" + member, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("Created path: " + path);
    }

    public static void main(String[] args) {
        JoinGroup joinGroup = new JoinGroup();
        try {
            joinGroup.connect("192.168.2.135:2181");
            joinGroup.join("zoo", args[0]);

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