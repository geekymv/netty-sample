package com.geekymv.netty.sample.concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Worker implements Runnable {

    private int myRow;
    private CyclicBarrier barrier;


    public Worker(int myRow, CyclicBarrier barrier) {
        this.myRow = myRow;
        this.barrier = barrier;
    }

    @Override
    public void run() {

        boolean f = true;

        while (f) {
            processRow(myRow);

            try {

                System.out.println("await before " + Thread.currentThread().getName());
                barrier.await();
                System.out.println("await after " + Thread.currentThread().getName());

                f = false;

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

    }

    private void processRow(int myRow) {
        System.out.println("处理 myRow = " + myRow);
    }
}
