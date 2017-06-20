package com.xym.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;

/**
 * 使用curator分布式计数器
 *
 * @author xym
 */
public class DistributedAtomicIntTest {
    public static void main(String[] args) {
        String path = "/zk-count";
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString("192.168.2.135:2181").retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
        curatorFramework.start();
        DistributedAtomicInteger distributedAtomicInteger = new DistributedAtomicInteger(curatorFramework, path, new RetryNTimes(3, 1000));

        try {
            AtomicValue<Integer> result = distributedAtomicInteger.add(8);
            System.out.println("Result:" + result.succeeded());
            byte[] bytes = curatorFramework.getData().forPath(path);
            System.out.println(new String(bytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}