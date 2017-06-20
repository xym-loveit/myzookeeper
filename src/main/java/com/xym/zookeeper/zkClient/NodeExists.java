package com.xym.zookeeper.zkClient;

import org.I0Itec.zkclient.ZkClient;

/**
 * 检测节点是否存在
 *
 * @author xym
 */
public class NodeExists {

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient("192.168.2.135", 5000);
        boolean ex = zkClient.exists("/node2");
        System.out.println(ex);
        zkClient.createEphemeral("/node2");
        System.out.println(zkClient.exists("/node2"));
        zkClient.close();
    }


}