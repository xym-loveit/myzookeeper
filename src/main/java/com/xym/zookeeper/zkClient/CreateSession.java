package com.xym.zookeeper.zkClient;

import org.I0Itec.zkclient.ZkClient;

/**
 * 使用zkclient创建会话
 *
 * @author xym
 * @create 2017-06-20 15:03
 */
public class CreateSession {

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient("192.168.2.135:2181", 5000, 30000);
        System.out.println("会话已创建");
    }
}