package com.xym.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;

import java.io.File;
import java.nio.file.Files;

/**
 * desc
 *
 * @author xym
 */
public class TestServerTest {
    public static void main(String[] args) {
        try {
            TestingServer server = new TestingServer(2181, new File("c:/zk"));
            CuratorFramework build = CuratorFrameworkFactory.builder().connectString(server.getConnectString()).sessionTimeoutMs(5000).connectionTimeoutMs(3000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
            build.start();
            System.out.println(build.getChildren().forPath("/zookeeper"));
            server.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}