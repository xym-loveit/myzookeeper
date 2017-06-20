package com.xym.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用curator异步方式创建节点
 *
 * @author xym
 */
public class CreateNodeBackground {

    static String path = "/node1/c1";
    static CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString("192.168.2.135:2181").connectionTimeoutMs(3000).sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();

    static ExecutorService executors = Executors.newFixedThreadPool(2);
    static CountDownLatch semaphore = new CountDownLatch(2);

    public static void main(String[] args) {
        curatorFramework.start();
        System.out.println("Main Thread " + Thread.currentThread().getName());
        try {
            //使用自定义线程池做任务
            curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).inBackground(new BackgroundCallback() {
                public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                    System.out.println(event.getResultCode() + "--" + event.getType());
                    System.out.println("current thread=" + Thread.currentThread().getName());
                    semaphore.countDown();
                }
            }, executors).forPath(path, "init".getBytes());

            //使用默认的curator的EventThread
            curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).inBackground(new BackgroundCallback() {
                public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                    System.out.println(event.getResultCode() + "--" + event.getType());
                    System.out.println("current thread=" + Thread.currentThread().getName());
                    semaphore.countDown();
                }
            }).forPath(path, "init".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {


            //保证2个线程都执行完毕再停止
            semaphore.await();
            executors.shutdown();
            curatorFramework.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}