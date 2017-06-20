package com.xym.zookeeper.zkapi
        ;

import org.apache.zookeeper.AsyncCallback;

/**
 * desc
 *
 * @author xym
 * @create 2017-06-20 14:52
 */ //异步回调接口
class IStringCallBack implements AsyncCallback.StringCallback {
    public void processResult(int rc, String path, Object ctx, String name) {
        System.out.println("创建节点结果：[" + rc + "," + path + "," + ctx + ",real path name:" + name + "]");
    }

}