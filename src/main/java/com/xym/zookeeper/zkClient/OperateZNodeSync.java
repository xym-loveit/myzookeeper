package com.xym.zookeeper.zkClient;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * zk JavaAPI同步操作znode
 * Created by xym on 2017/6/29.
 */
public class OperateZNodeSync implements Watcher {

    private static ZooKeeper zooKeeper;
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static String connStr = "192.168.2.135:2181";
    private static final String PATH = "/zk-study";

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        OperateZNodeSync operateZNodeSync = new OperateZNodeSync();
        zooKeeper = new ZooKeeper(connStr, 5000, operateZNodeSync);

        //删除节点及其子节点
        if (operateZNodeSync.existsNode()) {
            for (String children : operateZNodeSync.getChildrens()) {
                operateZNodeSync.delChildren(PATH + "/" + children);
            }
            operateZNodeSync.delNode();
        }

        operateZNodeSync.create();
        operateZNodeSync.createChildNode();
        operateZNodeSync.getChildrens();

        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }

    /**
     * 创建节点
     *
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void create() throws KeeperException, InterruptedException {
        String path = zooKeeper.create(PATH, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("create: " + path);
    }

    /**
     * 获取节点数据
     *
     * @throws KeeperException
     * @throws InterruptedException
     */
    public Stat getData() throws KeeperException, InterruptedException {
        //stat 引用替换，获取当前节点状态，就是客户端命令行stat命令执行结果
        Stat stat = new Stat();
        byte[] bytes = zooKeeper.getData(PATH, true, stat);
        System.out.println("getDate: " + new String(bytes));
        System.out.println(stat);
        return stat;
    }

    /**
     * 获取子节点列表
     *
     * @throws KeeperException
     * @throws InterruptedException
     */
    public List<String> getChildrens() throws KeeperException, InterruptedException {
        List<String> zooKeeperChildren = zooKeeper.getChildren(PATH, true);
        System.out.println("getChildrens: " + zooKeeperChildren);
        return zooKeeperChildren;
    }

    public boolean existsNode() throws KeeperException, InterruptedException {
        Stat stat = zooKeeper.exists(PATH, true);
        return
                stat != null ? true : false;
    }

    /**
     * 删除节点
     *
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void delNode() throws KeeperException, InterruptedException {
        zooKeeper.delete(PATH, -1);
    }

    /**
     * 删除指定子节点
     *
     * @param children
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void delChildren(String children) throws KeeperException, InterruptedException {
        zooKeeper.delete(children, -1);
    }

    /**
     * 创建子节点
     *
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void createChildNode() throws KeeperException, InterruptedException {
        String pathC = zooKeeper.create(PATH + "/c1", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("createChildNode:" + pathC);
    }

    public void process(WatchedEvent event) {
        //已连接上
        if (event.getState() == Event.KeeperState.SyncConnected) {
            //表示连接上
            if (event.getType() == Event.EventType.None && null == event.getPath()) {
                countDownLatch.countDown();
            } else if (event.getType() == Event.EventType.NodeChildrenChanged) {
                System.out.println("NodeChildrenChanged--" + event.getPath());
            } else if (event.getType() == Event.EventType.NodeCreated) {
                System.out.println("NodeCreated--" + event.getPath());
            } else if (event.getType() == Event.EventType.NodeDeleted) {
                System.out.println("NodeDeleted--" + event.getPath());
            } else if (event.getType() == Event.EventType.NodeDataChanged) {
                System.out.println("NodeDataChanged--" + event.getPath());
            }
        }
    }
}
