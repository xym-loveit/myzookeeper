package com.xym.zookeeper.zkClient;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * zk JavaAPI异步操作znode
 * Created by xym on 2017/6/29.
 */
public class OperateZNodeASync implements Watcher {

    private static ZooKeeper zooKeeper;
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static String connStr = "192.168.2.135:2181";
    private static final String PATH = "/zk-study";

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        OperateZNodeASync operateZNodeSync = new OperateZNodeASync();
        zooKeeper = new ZooKeeper(connStr, 5000, operateZNodeSync);


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
    public void create() {
        zooKeeper.create(PATH, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, new IStringCallBack(), "im create");
    }

    /**
     * 获取节点数据
     *
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void getData() throws KeeperException, InterruptedException {
        zooKeeper.getData(PATH, true, new IDataCallBack(), "im getDate");
    }

    /**
     * 获取子节点列表
     *
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void getChildrens() throws KeeperException, InterruptedException {
        zooKeeper.getChildren(PATH, true, new IChildren2Callback(), "im getChildrens");
    }

    public void existsNode() throws KeeperException, InterruptedException {
        zooKeeper.exists(PATH, true, new IStatCallBack(), "im existsNode");
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


    class IStringCallBack implements AsyncCallback.StringCallback {
        public void processResult(int rc, String path, Object ctx, String name) {
            StringBuffer sb = new StringBuffer("creating...\n");
            sb.append("rc=" + rc).append("\n");
            sb.append("path=" + path).append("\n");
            sb.append("ctx=" + ctx).append("\n");
            sb.append("name=" + name);
            System.out.println(sb.toString());
        }
    }

    class IDataCallBack implements AsyncCallback.DataCallback {
        public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
            StringBuffer sb = new StringBuffer("getData...\n");
            sb.append("rc=" + rc).append("\n");
            sb.append("data=" + data);
            sb.append("path=" + path).append("\n");
            sb.append("ctx=" + ctx).append("\n");
            sb.append("stat=" + stat);
            System.out.println(sb.toString());
        }
    }

    class IChildren2Callback implements AsyncCallback.Children2Callback {
        public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
            StringBuffer sb = new StringBuffer("getChildren...\n");
            sb.append("rc=" + rc).append("\n");
            sb.append("children=" + children);
            sb.append("path=" + path).append("\n");
            sb.append("ctx=" + ctx).append("\n");
            sb.append("stat=" + stat);
            System.out.println(sb.toString());
        }
    }

    class IStatCallBack implements AsyncCallback.StatCallback {
        public void processResult(int rc, String path, Object ctx, Stat stat) {
            StringBuffer sb = new StringBuffer("exists...\n");
            sb.append("rc=" + rc).append("\n");
            sb.append("path=" + path).append("\n");
            sb.append("ctx=" + ctx).append("\n");
            sb.append("stat=" + stat);
            System.out.println(sb.toString());
        }
    }
}
