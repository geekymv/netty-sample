package com.geekymv.netty.sample.nio;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * http://zhangshixi.iteye.com/blog/679959
 * IO以流的方式处理数据
 * NIO以块的方式处理数据
 *
 */
public class NioTest00 {

    public void ioRead(String file) throws IOException {
        FileInputStream fis = new FileInputStream(file);

        byte[] buffer = new byte[1024];
        fis.read(buffer);

        System.out.println(new String(buffer, "utf-8"));
    }

    public void nioRead(String file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        FileChannel channel = fis.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer);

        byte[] b = buffer.array();
        System.out.println(new String(b, "utf-8"));
    }


}
