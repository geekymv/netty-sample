package com.geekymv.netty.sample.concurrent.multithread;

import org.junit.Test;

/**
 * 子线程运行10次后，主线程再运行5次。
 * 这样交替执行3遍。
 */
public class WaitNotifyTest {

    @Test
    public void test() {

        Business business = new Business();

        Thread subThread = new Thread(()-> {
            for(int i = 0; i < 3; i++) {
                business.subMethod();
            }
        });
        subThread.start();

        for(int i = 0; i < 3; i++) {
            business.mainMethod();
        }


        try {
            Thread.sleep(10 * 10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class Business {

    private volatile boolean subThread = true;

    public synchronized void subMethod() {
        // TODO 这个地方为什么用while而不是if
        while (!subThread) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for(int i = 0; i <  10; i++) {
            System.out.println("sub method = " + i);
        }
        notify();

        subThread = false;
    }


    public synchronized void mainMethod() {
        while (subThread) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for(int i = 0; i < 5; i++) {
            System.out.println("main method = " + i);
        }

        notify();
        subThread = true;
    }

}
