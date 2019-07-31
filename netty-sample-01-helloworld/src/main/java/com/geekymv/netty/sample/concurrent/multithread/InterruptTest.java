package com.geekymv.netty.sample.concurrent.multithread;

public class InterruptTest {

    public static void main(String[] args) {

        Thread t1 = new Thread(()-> {
            while (true) {
                System.out.println("test111");
            }
        });
        t1.start();

        try {
            Thread.sleep(3 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t1.interrupt();

        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
