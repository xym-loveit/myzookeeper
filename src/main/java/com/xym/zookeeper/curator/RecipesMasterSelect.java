package com.xym.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * master选举
 *
 * @author xym
 */
public class RecipesMasterSelect {

    static String path = "/recipes_master";
    static CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString("192.168.2.135:2181").retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();

    public static void main(String[] args) {
        curatorFramework.start();
        LeaderSelector leaderSelector = new LeaderSelector(curatorFramework, path, new LeaderSelectorListenerAdapter() {
            public void takeLeadership(CuratorFramework client) throws Exception {
                System.out.println("成为master角色");
                TimeUnit.SECONDS.sleep(3);
                System.out.println("完成master操作，释放master权利" + new Date().toLocaleString());
            }
        });
        leaderSelector.autoRequeue();
        leaderSelector.start();

        try {
            System.out.println("~~~~~~~~~~~~~~~~~~~~main--");
            TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}