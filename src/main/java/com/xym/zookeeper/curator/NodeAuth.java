package com.xym.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

/**
 * 对具有acl权限控制的节点访问需要使用authorization("digest", "xym:111")进行授权
 *
 * @author xym
 */
public class NodeAuth {

    private static CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().authorization("digest", "xym:111".getBytes()).connectString("192.168.2.135:2181").sessionTimeoutMs(5000).connectionTimeoutMs(5000).retryPolicy(new RetryNTimes(3, 1000)).build();

    public static void main(String[] args) throws Exception {
        curatorFramework.start();

        System.out.println(DigestAuthenticationProvider.generateDigest("xym:111"));
        //
        //List<ACL> acls = new ArrayList<ACL>();
        //ACL aclIp = new ACL(ZooDefs.Perms.READ, new Id("ip", "192.168.2.135"));
        //ACL aclDigest = new ACL(ZooDefs.Perms.READ | ZooDefs.Perms.WRITE, new Id("digest", DigestAuthenticationProvider.generateDigest("xym:111")));
        //acls.add(aclIp);
        //acls.add(aclDigest);
        //
        //String path = curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).withACL(acls).forPath("/zk-study", "www".getBytes());
        //System.out.println(path);


        byte[] bytes = curatorFramework.getData().forPath("/zk-study");
        System.out.println(new String(bytes));

        Thread.sleep(Integer.MAX_VALUE);
    }

}