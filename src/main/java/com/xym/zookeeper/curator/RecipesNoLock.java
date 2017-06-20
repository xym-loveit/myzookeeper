package com.xym.zookeeper.curator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * 使用时间戳也会出现并发问题
 *
 * @author xym
 */
public class RecipesNoLock {

    public static void main(String[] args) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        countDownLatch.await();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss|SSS");
                        String format = simpleDateFormat.format(new Date());
                        System.out.println("生成的订单号是：" + format);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        countDownLatch.countDown();
    }
}