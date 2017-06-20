package com.xym.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 采用fluent 流式编程创建会话
 *
 * @author xym
 */
public class CreateSessionFluent {

    public static void main(String[] args) {
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString("192.168.2.135:2181").sessionTimeoutMs(5000).connectionTimeoutMs(3000).retryPolicy(retry).build();

        curatorFramework.start();
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}