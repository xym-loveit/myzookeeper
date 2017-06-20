package com.xym.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * 使用Curator实现分布锁功能
 *
 * @author xym
 */
public class RecipesLock {
    public static void main(String[] args) {
        String lock = "/zk-lock";
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString("192.168.2.135:2181").retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        //保证所有的子线程都执行完
        final CountDownLatch countDownLatch2 = new CountDownLatch(100);
        curatorFramework.start();
        final Set<String> sets = new HashSet<String>();
        final InterProcessMutex mutex = new InterProcessMutex(curatorFramework, lock);
        for (int i = 0; i < 1000; i++) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        countDownLatch.await();
                        mutex.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String dateStr = new SimpleDateFormat("HH:mm:ss|SSS").format(new Date());
                    System.out.println("生成的订单号：" + dateStr);
                    sets.add(dateStr);
                    try {
                        mutex.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    countDownLatch2.countDown();
                }
            }).start();
        }
        countDownLatch.countDown();
        try {
            countDownLatch2.await();
            //看每次的size是否为100就知道了是否生成了重复值（set集合特性）
            System.out.println(sets.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}