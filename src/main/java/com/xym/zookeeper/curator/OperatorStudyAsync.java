package com.xym.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * desc
 *
 * @author xym
 */
public class OperatorStudyAsync {
    private static CuratorFramework curatorFramework = null;
    private static final String PATH = "/zk-study/c1";

    private ExecutorService es = Executors.newFixedThreadPool(3);

    public static void main(String[] args) throws Exception {
        curatorFramework = CuratorFrameworkFactory.builder().connectString("192.168.2.135:2181").connectionTimeoutMs(5000).sessionTimeoutMs(5000).retryPolicy(new RetryNTimes(3, 1000)).build();

        curatorFramework.start();

        OperatorStudyAsync operatorStudy = new OperatorStudyAsync();
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
        //每一个一步操作都会开启一个线程，如果异步操作较多就比较消耗资源(EventThread)
        //这种情况下，我们可以使用线程池来管理异步操作中创建的线程
        //其他异步操作雷同
        //curatorFramework.checkExists().inBackground(new BackgroundCallback() {
        //    public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
        //        System.out.println(event.getResultCode() + "," + event.getType().name() + "," + event.getContext() + "," + event.getData() + "," + event.getPath() + "," + event.getStat() + "," + event.getChildren() + "," + event.getName());
        //        System.out.println(Thread.currentThread().getName());
        //    }
        //}, "xxx", es).forPath(PATH);

        //curatorFramework.checkExists().inBackground(new BackgroundCallback() {
        //    public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
        //        System.out.println(event.getResultCode() + "," + event.getType().name() + "," + event.getContext() + "," + event.getData() + "," + event.getPath() + "," + event.getStat() + "," + event.getChildren() + "," + event.getName());
        //        System.out.println(Thread.currentThread().getName());
        //    }
        //}, "xxx").forPath(PATH);
    }
}