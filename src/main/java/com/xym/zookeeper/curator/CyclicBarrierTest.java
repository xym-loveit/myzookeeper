package com.xym.zookeeper.curator;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * java中循环屏障
 *
 * @author xym
 */
public class CyclicBarrierTest {
    static CyclicBarrier cyclicBarrier = new CyclicBarrier(3);

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.submit(new Thread(new Runner("1号选手")));
        executorService.submit(new Thread(new Runner("2号选手")));
        executorService.submit(new Thread(new Runner("3号选手")));

        executorService.shutdown();
    }
}

class Runner implements Runnable {
    private String name;

    public Runner(String name) {
        this.name = name;
    }

    public void run() {
        System.out.println(name + "准备好了！");
        try {
            CyclicBarrierTest.cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println(name + "起跑！");
    }
}