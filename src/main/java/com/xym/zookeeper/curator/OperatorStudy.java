package com.xym.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * desc
 *
 * @author xym
 */
public class OperatorStudy {
    private static CuratorFramework curatorFramework = null;
    private static final String PATH = "/zk-study/c1";

    public static void main(String[] args) throws Exception {
        curatorFramework = CuratorFrameworkFactory.builder().connectString("192.168.2.135:2181").connectionTimeoutMs(5000).sessionTimeoutMs(5000).retryPolicy(new RetryNTimes(3, 1000)).build();

        curatorFramework.start();

        OperatorStudy operatorStudy = new OperatorStudy();
        operatorStudy.create();
        //operatorStudy.getDate();
        //operatorStudy.deleteNode();
        //operatorStudy.setData();
        //operatorStudy.getChildren();
        operatorStudy.checkExists();

        Thread.sleep(Integer.MAX_VALUE);

    }


    public void create() throws Exception {
        String path = curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(PATH, "123".getBytes());
        System.out.println("path=" + path);
    }

    public void getData() throws Exception {
        Stat stat = new Stat();
        //获取状态和数据
        byte[] bytes = curatorFramework.getData().storingStatIn(stat).forPath("/zk-study");
        System.out.println("data=" + new String(bytes));
        System.out.println("stat=" + stat);
    }

    public void deleteNode() throws Exception {
        //guaranteed确保一定删除成功
        curatorFramework.delete().guaranteed().deletingChildrenIfNeeded().withVersion(-1).forPath("/zk-study");
    }

    public void setData() throws Exception {
        curatorFramework.setData().withVersion(-1).forPath("/zk-study", "123456".getBytes());
    }

    public void getChildren() throws Exception {
        List<String> lists = curatorFramework.getChildren().forPath("/zk-study");
        System.out.println("list=" + lists);
    }

    public void checkExists() throws Exception {
        Stat stat = curatorFramework.checkExists().forPath(PATH);
        System.out.println(stat);
    }
}