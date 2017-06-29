package com.xym.zookeeper.zkapi;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * desc
 *
 * @author xym
 */
public class ConfigUpdater {
    public final static String path = "/config";
    private ActiveKeyValueStore store;
    private Random random = new Random();

    public ConfigUpdater(String host) throws IOException, InterruptedException {
        this.store = new ActiveKeyValueStore();
        store.connect(host);
    }


    public void run() throws KeeperException, InterruptedException {
        while (true) {
            String anInt = random.nextInt(100) + "";
            store.write(path, anInt);
            System.out.printf("Set %s to %s \n", path, anInt);
            int time = random.nextInt(10);
            System.out.println("time=" + time);
            TimeUnit.SECONDS.sleep(time);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ConfigUpdater configUpdater = new ConfigUpdater("192.168.2.135:2181");
        configUpdater.run();
    }
}