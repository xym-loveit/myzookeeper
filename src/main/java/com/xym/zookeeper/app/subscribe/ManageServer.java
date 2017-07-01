package com.xym.zookeeper.app.subscribe;

import com.alibaba.fastjson.JSON;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;

import java.util.List;

/**
 * 模拟工作机服务器管理者
 */
public class ManageServer {

    private String serversPath;//工作机服务器注册父节点/servers
    private String commandPath;//命令节点/commands/用于向本工作机服务器管理者提供管理命令
    private String configPath;//配置信息节点/configs
    private ZkClient zkClient;
    private ServerConfig config;
    private IZkChildListener childListener;//servers子节点监控
    private IZkDataListener dataListener;//command节点内容监控
    private List<String> workServerList;//被监控服务器列表

    public ManageServer(String serversPath, String commandPath,
                        String configPath, ZkClient zkClient, ServerConfig config) {
        this.serversPath = serversPath;
        this.commandPath = commandPath;
        this.zkClient = zkClient;
        this.config = config;
        this.configPath = configPath;
        this.childListener = new IZkChildListener() {
            //被监控/servers节点变更事件
            public void handleChildChange(String parentPath,
                                          List<String> currentChilds) throws Exception {
                // TODO Auto-generated method stub
                workServerList = currentChilds;

                System.out.println("work server list changed, new list is ");
                execList();

            }
        };
        this.dataListener = new IZkDataListener() {

            public void handleDataDeleted(String dataPath) throws Exception {
                // TODO Auto-generated method stub
                // ignore;
            }

            public void handleDataChange(String dataPath, Object data)
                    throws Exception {
                // TODO Auto-generated method stub
                //被监控/commands内容变更
                String cmd = new String((byte[]) data);
                System.out.println("cmd:" + cmd);
                exeCmd(cmd);

            }
        };

    }

    private void initRunning() {
        //注册/commands命令节点内容变更和/servers子节点变更
        zkClient.subscribeDataChanges(commandPath, dataListener);
        zkClient.subscribeChildChanges(serversPath, childListener);
    }

    /*
     * 1: list 2: create 3: modify
     */
    private void exeCmd(String cmdType) {
        if ("list".equals(cmdType)) {
            execList();

        } else if ("create".equals(cmdType)) {
            execCreate();
        } else if ("modify".equals(cmdType)) {
            execModify();
        } else {
            System.out.println("error command!" + cmdType);
        }

    }

    private void execList() {
        System.out.println(workServerList.toString());
    }

    //创建/configs节点并写入配置信息
    private void execCreate() {
        if (!zkClient.exists(configPath)) {
            try {
                zkClient.createPersistent(configPath, JSON.toJSONString(config)
                        .getBytes());
            } catch (ZkNodeExistsException e) {
                //如果当前节点已创建则覆盖其配置
                zkClient.writeData(configPath, JSON.toJSONString(config)
                        .getBytes());
            } catch (ZkNoNodeException e) {
                //如果父节点不存在则先创建父节点在执行函数
                String parentDir = configPath.substring(0,
                        configPath.lastIndexOf('/'));
                zkClient.createPersistent(parentDir, true);
                execCreate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //修改/configs节点数据
    private void execModify() {
        config.setDbUser(config.getDbUser() + "_modify");

        try {
            zkClient.writeData(configPath, JSON.toJSONString(config).getBytes());
        } catch (ZkNoNodeException e) {
            execCreate();
        }
    }

    public void start() {
        initRunning();
    }

    //解除掉init初始化时注册的事件
    public void stop() {
        zkClient.unsubscribeChildChanges(serversPath, childListener);
        zkClient.unsubscribeDataChanges(commandPath, dataListener);
    }
}
