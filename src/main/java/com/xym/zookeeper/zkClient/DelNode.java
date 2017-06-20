package com.xym.zookeeper.zkClient;

import org.I0Itec.zkclient.ZkClient;

import java.util.List;

/**
 * 删除节点
 *
 * @author xym
 */
public class DelNode {

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient("192.168.2.135:2181", 5000);
        zkClient.createPersistent("/aaa");
        boolean delete = zkClient.delete("/aaa");
        System.out.println(delete);
        zkClient.createPersistent("/a/b/c", true);

        //递归删除
        boolean recursive = zkClient.deleteRecursive("/a");
        System.out.println(recursive);
        node(zkClient, zkClient.getChildren("/"), "/");

    }

    private static void node(ZkClient zkClient, List<String> nodes, String rootP) {
        String path = null;
        for (String node : nodes) {

            if (rootP.equals("/")) {
                path = rootP + node;
            } else {
                path = rootP + "/" + node;
            }
            System.out.println(path);
            node(zkClient, zkClient.getChildren(path), path);
        }
    }
}