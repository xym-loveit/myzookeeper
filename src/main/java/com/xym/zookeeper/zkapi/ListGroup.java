package com.xym.zookeeper.zkapi;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.util.List;

/**
 * desc
 *
 * @author xym
 */
public class ListGroup extends ConnectionWatcher {

    public void list(String groupName) throws KeeperException, InterruptedException {
        List<String> zkChildren = zk.getChildren("/" + groupName, false);
        if (zkChildren.isEmpty()) {
            System.out.printf("No members in group %s\n", groupName);
            System.exit(1);
        }

        for (String zkChild : zkChildren) {
            System.out.println(zkChild);
        }

    }

    public static void main(String[] args) {
        ListGroup listGroup = new ListGroup();
        try {
            listGroup.connect("192.168.2.135:2181");
            listGroup.list("zoo");
            listGroup.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }
}