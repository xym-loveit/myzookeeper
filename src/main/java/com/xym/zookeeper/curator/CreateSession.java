package com.xym.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 使用curator创建一个会话
 *
 * @author xym
 */
public class CreateSession {

    public static void main(String[] args) {

        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);

        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("192.168.2.135", 5000, 3000, retryPolicy);

        curatorFramework.start();

        try {
            Thread.sleep(5000);

            curatorFramework.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}