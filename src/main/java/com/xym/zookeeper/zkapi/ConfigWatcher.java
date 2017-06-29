package com.xym.zookeeper.zkapi;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * desc
 *
 * @author xym
 */
public class ConfigWatcher implements Watcher {

    private ActiveKeyValueStore store;


    public void process(WatchedEvent event) {
        if (event.getType() == Event.EventType.NodeDataChanged) {
            displayConfig();
        }
    }

    public ConfigWatcher(String host) throws IOException, InterruptedException {
        this.store = new ActiveKeyValueStore();
        store.connect(host);
    }

    public static void main(String[] args) {
        try {
            ConfigWatcher configWatcher = new ConfigWatcher("192.168.2.135:2181");
            configWatcher.displayConfig();


            Thread.sleep(Integer.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void displayConfig() {
        try {
            String result = this.store.read(ConfigUpdater.path, this);
            System.out.printf("Read %s as %s\n", ConfigUpdater.path, result);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}