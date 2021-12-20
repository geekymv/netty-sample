package com.geekymv.netty.sample.nio.buffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class TestByteBuffer {

    public static void main(String[] args) {

        try (FileChannel channel = new FileInputStream("data.txt").getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            int len;
            // 从 channel 中读取字节放入 buffer 中
            while ((len = channel.read(buffer)) > 0) {
                System.out.println("读取到的字节数 len = " + len);

                // 切换至读模式
                buffer.flip();
                while (buffer.hasRemaining()) {
                    System.out.println((char)buffer.get());
                }

                // 清空缓存，切换到写模式
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
