package com.xym.zookeeper.zkapi;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.util.List;

/**
 * desc
 *
 * @author xym
 */
public class DeleteGroup extends ConnectionWatcher {

    public static void main(String[] args) {
        DeleteGroup deleteGroup = new DeleteGroup();
        try {
            deleteGroup.connect("192.168.2.135:2181");
            deleteGroup.delete("zoo");
            deleteGroup.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }


    public void delete(String groupName) throws KeeperException, InterruptedException {
        List<String> zkChildren = zk.getChildren("/" + groupName, false);
        if (!zkChildren.isEmpty()) {
            for (String zkChild : zkChildren) {
                zk.delete("/" + groupName + "/" + zkChild, -1);
            }
            zk.delete("/" + groupName, -1);
        }

    }

}