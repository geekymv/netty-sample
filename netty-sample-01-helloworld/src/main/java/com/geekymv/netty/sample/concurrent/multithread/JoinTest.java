package com.geekymv.netty.sample.concurrent.multithread;

import java.time.LocalDateTime;

/**
 * https://juejin.im/post/5ae6cf7a518825670960fcc2#heading-7
 * join 方法是线程间协作的一种方式
 */
public class JoinTest {

    public static void main(String[] args) {

        Thread t1 = new Thread(()-> {
            System.out.println("t1..." + LocalDateTime.now());
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();

        try {
            // 当前线程(主线程)会等待t1执行完后才会继续执行
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("main..." + LocalDateTime.now());
    }

}
