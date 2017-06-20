package com.xym.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;

/**
 * 使用curator实现barrier
 *
 * @author xym
 */
public class DistributedBarrierTest {
    static String path = "/zk-barrier";
    static DistributedBarrier distributedBarrier = null;

    public static void main(String[] args) {


        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                public void run() {

                    CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString("192.168.2.135:2181").retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
                    curatorFramework.start();
                    distributedBarrier = new DistributedBarrier(curatorFramework, path);
                    System.out.println(Thread.currentThread().getName() + " 号barrier设置");

                    try {
                        //
                        distributedBarrier.setBarrier();
                        //等待barrier释放
                        distributedBarrier.waitOnBarrier();
                        System.out.println("启动...");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }).start();
        }
        try {
            TimeUnit.SECONDS.sleep(5);
            //主线程释放barrier触发waitOnBarrier后面的业务执行
            distributedBarrier.removeBarrier();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}