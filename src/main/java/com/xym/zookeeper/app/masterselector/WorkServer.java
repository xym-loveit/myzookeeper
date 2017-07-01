package com.xym.zookeeper.app.masterselector;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkException;
import org.I0Itec.zkclient.exception.ZkInterruptedException;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WorkServer {

    //服务器运行标记,volatile确保可见性
    private volatile boolean running = false;

    private ZkClient zkClient;

    //master争抢创建的节点
    private static final String MASTER_PATH = "/master";

    //监听
    private IZkDataListener dataListener;

    //当前服务器数据
    private RunningData serverData;
    //当前master服务器数据
    private RunningData masterData;

    //延迟调度器
    private ScheduledExecutorService delayExector = Executors.newScheduledThreadPool(1);
    private int delayTime = 5;//延迟时间

    public WorkServer(RunningData rd) {
        this.serverData = rd;
        this.dataListener = new IZkDataListener() {
            //删除调用
            public void handleDataDeleted(String dataPath) throws Exception {
                // TODO Auto-generated method stub

                //takeMaster();

                //防止网络抖动，如果当前删除的master节点就是当前服务器本身那么发起争抢master操作
                if (masterData != null && masterData.getName().equals(serverData.getName())) {
                    takeMaster();

                } else {
                    //如果当前删除的master节点不是当前服务器那么延迟5秒再去争抢master
                    delayExector.schedule(new Runnable() {
                        public void run() {
                            takeMaster();
                        }
                    }, delayTime, TimeUnit.SECONDS);

                }


            }

            public void handleDataChange(String dataPath, Object data)
                    throws Exception {
                // TODO Auto-generated method stub

            }
        };
    }

    public ZkClient getZkClient() {
        return zkClient;
    }

    public void setZkClient(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

    public void start() throws Exception {
        if (running) {
            throw new Exception("server has startup...");
        }
        //服务器启动时，订阅master，然后争抢master
        running = true;
        zkClient.subscribeDataChanges(MASTER_PATH, dataListener);
        takeMaster();

    }

    public void stop() throws Exception {
        if (!running) {
            throw new Exception("server has stoped");
        }
        running = false;

        delayExector.shutdown();
        //释放master同时解除监听
        zkClient.unsubscribeDataChanges(MASTER_PATH, dataListener);
        //服务器停止则释放master
        releaseMaster();

    }

    //获取master
    private void takeMaster() {
        if (!running)
            return;

        try {
            //创建master临时节点，如成功此服务器作为master，其他节点作为slave，值为机器信息
            zkClient.create(MASTER_PATH, serverData, CreateMode.EPHEMERAL);
            masterData = serverData;
            System.out.println(serverData.getName() + " is master");

            //为了演示争抢master操作，5秒钟释放一次master节点
            delayExector.schedule(new Runnable() {
                public void run() {
                    // TODO Auto-generated method stub
                    if (checkMaster()) {
                        releaseMaster();
                    }
                }
            }, 5, TimeUnit.SECONDS);

        } catch (ZkNodeExistsException e) {
            //如果节点已存在，说明当前已有master则去读取数据
            RunningData runningData = zkClient.readData(MASTER_PATH, true);
            //读取数据的瞬间master如果挂掉,则去争抢master
            if (runningData == null) {
                takeMaster();
            } else {
                //如果master正常，则为当前服务器master属性赋值
                masterData = runningData;
            }
        } catch (Exception e) {
            // ignore;
        }

    }

    //删掉znode节点释放master
    private void releaseMaster() {
        if (checkMaster()) {
            zkClient.delete(MASTER_PATH);

        }

    }

    //检查当前服务器是不是master
    private boolean checkMaster() {
        try {
            RunningData eventData = zkClient.readData(MASTER_PATH);
            masterData = eventData;
            //通过对比当前服务器和master属性名判断当前服务器是否是master
            if (masterData.getName().equals(serverData.getName())) {
                return true;
            }
            return false;
        } catch (ZkNoNodeException e) {
            return false;
        } catch (ZkInterruptedException e) {
            //如果当前操作被中断则重试
            return checkMaster();
        } catch (ZkException e) {
            return false;
        }
    }

}
