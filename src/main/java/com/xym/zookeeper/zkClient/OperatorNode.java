package com.xym.zookeeper.zkClient;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;

import java.io.Serializable;

/**
 * desc
 *
 * @author xym
 */
public class OperatorNode {
    public static void main(String[] args) throws InterruptedException {
        ZkClient zkClient = new ZkClient("192.168.2.135:2181", 5000, 5000, new SerializableSerializer());
        //zkClient.addAuthInfo("ip", "192.168.2.136".getBytes());
        String path = zkClient.create("/zk-study", new Student(1, "zhangsan"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);


        System.out.println(path);

        Thread.sleep(Integer.MAX_VALUE);
    }

    static class Student implements Serializable {
        private int id;
        private String name;

        public Student(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}