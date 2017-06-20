package com.xym.zookeeper.zkClient;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

/**
 * 获取节点数据，并注册监听器（监听数据变更及znode被删除）
 *
 * @author xym
 */
public class GetData {
    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient("192.168.2.135:2181", 5000);
        zkClient.createEphemeral("/node2", "123");
        zkClient.subscribeDataChanges("/node2", new IZkDataListener() {
            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println(dataPath + " DataChange,New Data=" + data);
            }

            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println(dataPath + " Deleted");
            }
        });

        Object readData = zkClient.readData("/node2");
        System.out.println(readData);
        zkClient.writeData("/node2", "456");

        try {
            Thread.sleep(1000);
            zkClient.delete("/node2");
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}