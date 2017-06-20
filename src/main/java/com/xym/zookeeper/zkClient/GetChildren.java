package com.xym.zookeeper.zkClient;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

/**
 * 获取子节点，注册监听
 *
 * @author xym
 */
public class GetChildren {
    public static void main(String[] args) {
        String path = "/aa";
        ZkClient zkClient = new ZkClient("192.168.2.135:2181", 5000);
        zkClient.subscribeChildChanges(path, new IZkChildListener() {
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                System.out.println(parentPath + "--" + currentChilds);
            }
        });

        try {
            zkClient.createPersistent(path);
            Thread.sleep(1000);
            System.out.println(zkClient.getChildren(path));
            zkClient.createPersistent(path + "/c1");
            Thread.sleep(1000);
            zkClient.createPersistent(path + "/c2");
            Thread.sleep(1000);
            zkClient.createPersistent(path + "/c3");
            Thread.sleep(1000);
            zkClient.deleteRecursive(path);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}