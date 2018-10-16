package com.geekymv.netty.sample.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class Solver {

    final int N;

    final float[][] data;

    final CyclicBarrier barrier;


    public Solver(float[][] matrix) {
        data = matrix;
        N = matrix.length;

        Runnable barrierAction = new Runnable() {
            @Override
            public void run() {
                System.out.println("barrierAction run...");
            }
        };

        barrier = new CyclicBarrier(N, barrierAction);
        List<Thread> threads = new ArrayList<>(N);

        // 创建线程并启动
        for(int i = 0; i < N; i++) {
            Thread thread = new Thread(new Worker(i, barrier));
            thread.setName("t-" + i);
            threads.add(thread);
            thread.start();
        }


        System.out.println("thread join before");
        try {
            for (Thread thread : threads) {
                thread.join();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("thread join after");

    }
}
