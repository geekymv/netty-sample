package com.geekymv.netty.sample.nio;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class NioTest00Test {

    private NioTest00 nioTest00;

    @Before
    public void before() {
        nioTest00 = new NioTest00();
    }

    @Test
    public void testIORead() {
        try {
            nioTest00.ioRead("D:/test.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNIORead() {
        try {
            nioTest00.nioRead("D:/test.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
