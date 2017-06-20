package com.xym.zookeeper.zkClient;

import org.I0Itec.zkclient.ZkClient;

/**
 * 创建节点
 *
 * @author xym
 */
public class CreateNode {

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient("192.168.2.135", 5000);
        if (!zkClient.exists("/zk-node1/c1")) {
            //创建节点，可递归
            zkClient.createPersistent("/zk-node1/c1", true);
        }

        for (String s : zkClient.getChildren("/")) {
            System.out.println("/" + s);
            if (zkClient.countChildren("/" + s) > 0) {
                for (String s1 : zkClient.getChildren("/" + s)) {
                    System.out.println("/" + s + "/" + s1);
                }
            }
        }
    }

}