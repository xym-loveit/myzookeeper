package com.xym.zookeeper.zkClient;

import org.I0Itec.zkclient.ZkClient;

/**
 * 更新node的值
 *
 * @author xym
 */
public class SetData {
    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient("192.168.2.135:2181", 5000);
        zkClient.createEphemeral("/node2", "123");

        zkClient.writeData("/node2", "456", -1);
        Object node2 = zkClient.readData("/node2");
        System.out.println(node2);

        zkClient.close();
    }
}