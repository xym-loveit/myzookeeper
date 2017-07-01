package com.xym.zookeeper.app.subscribe;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;

import com.alibaba.fastjson.JSON;

/**
 * 模拟工作机服务器
 */
public class WorkServer {

    private ZkClient zkClient;
    private String configPath;//config节点路径/configs
    private String serversPath;//server节点路径/servers
    private ServerData serverData;//当前工作机信息
    private ServerConfig serverConfig;//当前工作机中使用的配置信息
    private IZkDataListener dataListener;//工作机监听

    public WorkServer(String configPath, String serversPath,
                      ServerData serverData, ZkClient zkClient, ServerConfig initConfig) {
        this.zkClient = zkClient;
        this.serversPath = serversPath;
        this.configPath = configPath;
        this.serverConfig = initConfig;
        this.serverData = serverData;

        this.dataListener = new IZkDataListener() {

            public void handleDataDeleted(String dataPath) throws Exception {
                // TODO Auto-generated method stub

            }

            //监听全局配置/configs节点数据变更
            public void handleDataChange(String dataPath, Object data)
                    throws Exception {
                // TODO Auto-generated method stub
                String retJson = new String((byte[]) data);
                ServerConfig serverConfigLocal = (ServerConfig) JSON.parseObject(retJson, ServerConfig.class);
                updateConfig(serverConfigLocal);
                System.out.println("new Work server config is:" + serverConfig.toString());

            }
        };

    }

    public void start() {
        System.out.println("work server start...");
        initRunning();

    }

    public void stop() {
        System.out.println("work server stop...");
        //解除/configs事件监听
        zkClient.unsubscribeDataChanges(configPath, dataListener);
    }

    private void initRunning() {

        registMe();
        //订阅/config节点数据更改事件
        zkClient.subscribeDataChanges(configPath, dataListener);

    }

    //通过在/servers节点下创建临时子节点注册自己如/servers/ip
    private void registMe() {
        String mePath = serversPath.concat("/").concat(serverData.getAddress());

        try {
            zkClient.createEphemeral(mePath, JSON.toJSONString(serverData)
                    .getBytes());
        } catch (ZkNoNodeException e) {
            zkClient.createPersistent(serversPath, true);
            registMe();
        }
    }

    //更新本身配置属性
    private void updateConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

}
