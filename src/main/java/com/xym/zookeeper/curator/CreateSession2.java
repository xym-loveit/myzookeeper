package com.xym.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * desc
 *
 * @author xym
 */
public class CreateSession2 {

    public static void main(String[] args) throws Exception {

        //开始间隔1m重试，每重试一次间隔时间变长，但最多只能重试3次
        //RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        //指定间隔时间，重试指定次数
        //RetryNTimes retryNTimes = new RetryNTimes(3, 1000);
        //指定总重试时间和重试间隔时间
        RetryUntilElapsed retryUntilElapsed = new RetryUntilElapsed(5000, 1000);

        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("192.168.2.135:2181", 5000, 3000, retryUntilElapsed);

        curatorFramework.start();

        Stat stat = new Stat();
        List<String> list = curatorFramework.getChildren().storingStatIn(stat).forPath("/");
        System.out.println(list);
        System.out.println(stat);

        Thread.sleep(Integer.MAX_VALUE);
    }

}