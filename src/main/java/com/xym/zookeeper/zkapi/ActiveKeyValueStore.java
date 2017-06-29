package com.xym.zookeeper.zkapi;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * desc
 *
 * @author xym
 */
public class ActiveKeyValueStore extends ConnectionWatcher {

    private static final Charset CHARSET = Charset.forName("UTF-8");

    public String read(String path, Watcher watcher) throws KeeperException, InterruptedException, UnsupportedEncodingException {
        byte[] bytes = zk.getData(path, watcher, null);
        return new String(bytes, "UTF-8");
    }

    public void write(String path, String value) throws KeeperException, InterruptedException {
        Stat stat = zk.exists(path, false);
        if (stat == null) {
            zk.create(path, value.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } else {
            zk.setData(path, value.getBytes(), -1);
        }
    }


}