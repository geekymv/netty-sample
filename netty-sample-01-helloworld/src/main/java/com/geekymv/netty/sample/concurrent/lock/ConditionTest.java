package com.geekymv.netty.sample.concurrent.lock;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionTest {

    private final Lock lock = new ReentrantLock();

    private final Condition done = lock.newCondition();

    private volatile boolean flag = false;

    private AtomicInteger number = new AtomicInteger(0);


    public static void main(String[] args) {

        ConditionTest conditionTest = new ConditionTest();

        new Thread(()-> {
            while (true) {
                conditionTest.producer();
            }
        }).start();

        new Thread(()-> {
            while (true) {
                conditionTest.consumer();
            }
        }).start();

    }


    public void producer() {

        try {
            lock.lock();
            while (flag) {
                try {
                    done.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + "-----生产-----" + number.incrementAndGet());
            done.signal();
            flag = true;
        }finally {
            lock.unlock();
        }
    }


    public void consumer() {

        try {
            lock.lock();
            while (!flag) {
                try {
                    done.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + "-----消费-----" + number.decrementAndGet());
            flag = false;
            done.signal();
        }finally {
            lock.unlock();
        }
    }

}
