package com.geekymv.netty.sample.concurrent.multithread;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 使用定时器间隔4s执行一次，再间隔2s执行一次。以此类推
 */
public class TimerTest {

    private static volatile int count = 1;

    @Test
    public void test() {
        System.out.println("定时器启动：" + LocalDateTime.now());
        new Timer().schedule(new MyTimeTask(), 2000 + 2000 * count);

        try {
            Thread.sleep(10 * 10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class MyTimeTask extends TimerTask {

        @Override
        public void run() {
            System.out.println("定时器执行：" + LocalDateTime.now());
            count = (count + 1) % 2;
            new Timer().schedule(new MyTimeTask(), 2000 + 2000 * count);
        }
    }
}


