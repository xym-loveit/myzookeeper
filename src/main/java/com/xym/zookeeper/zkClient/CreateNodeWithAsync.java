package com.xym.zookeeper.zkClient;

import org.I0Itec.zkclient.ZkClient;

/**
 * desc
 *
 * @author xym
 */
public class CreateNodeWithAsync {
    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient("192.168.2.135:2181", 5000);
        zkClient.createPersistent("/zk-n1","test");
    }
}