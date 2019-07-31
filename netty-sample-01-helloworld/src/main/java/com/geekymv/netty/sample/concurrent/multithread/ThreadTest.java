package com.geekymv.netty.sample.concurrent.multithread;

public class ThreadTest {

    public static void main(String[] args) {

        Thread t1 = new ThreadA();
        t1.start();

        Thread t2 = new Thread(new ThreadB());
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class ThreadA extends Thread {
    @Override
    public void run() {
        System.out.println("继承Thread");
    }
}


class ThreadB implements Runnable {
    @Override
    public void run() {
        System.out.println("实现Runnable");
    }
}

