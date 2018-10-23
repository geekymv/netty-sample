package com.geekymv.netty.sample.thread;

import io.netty.util.concurrent.DefaultThreadFactory;
import org.junit.Test;


public class ThreadTest {

    @Test
    public void test1() {
        DefaultThreadFactory factory = new DefaultThreadFactory(getClass());
        Thread t = factory.newThread(()-> {

        });
    }

}
