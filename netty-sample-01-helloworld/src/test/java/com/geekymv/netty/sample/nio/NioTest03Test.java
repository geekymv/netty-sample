package com.geekymv.netty.sample.nio;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class NioTest03Test {

    private NioTest03 nioTest03;

    @Before
    public void before() {
        nioTest03 = new NioTest03();
    }

    @Test
    public void testWriteFile() {
        try {
            nioTest03.writeFile("D:/out.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
