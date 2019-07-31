package com.geekymv.netty.sample.threadpool;

import io.netty.util.concurrent.DefaultThreadFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorTest {

    private static final Logger logger = LoggerFactory.getLogger(ExecutorTest.class);

    /**
     * 当前运行的线程数 >= corePoolSize，
     *  ThreadPoolExecutor会将新的请求放入Queue中，
     *  如果放入失败
     *      目前运行的线程数 < maximumPoolSize，直接启动新线程来处理。
     *      目前运行的线程数 = maximumPoolSize，则执行对应的策略（默认是拒绝并抛出异常的策略）。
     *  放入成功
     *      再次check（当前运行的线程数是不是小于corePoolSize，线程池是不是被shutdown了）
     *
     *  SynchronousQueue 当往这个队列
     */
    ThreadPoolExecutor executor  = new ThreadPoolExecutor(10, 20,
            5, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10), new DefaultThreadFactory("my-pool"));
    @Test
    public void test() {

        new Thread(()-> {
            while (true) {
                int corePoolSize = executor.getCorePoolSize();
                int maximumPoolSize = executor.getMaximumPoolSize();
                int activeCount = executor.getActiveCount();
                BlockingQueue<Runnable> queue = executor.getQueue();
                int queueSize = queue.size();
                if(queueSize != 0) {
                    logger.info("corePoolSize = " + corePoolSize + ", activeCount = " + activeCount + ", maximumPoolSize = " + maximumPoolSize + ", queueSize = " + queueSize);
                }
            }
        }).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < 30; i++) {
            final int tmp = i;
            logger.info(tmp + " add ");
            executor.execute(()-> {
                try {
                    Thread.sleep(20 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                logger.info(tmp + ", hello");
            });
        }


        try {
            Thread.sleep(20 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
