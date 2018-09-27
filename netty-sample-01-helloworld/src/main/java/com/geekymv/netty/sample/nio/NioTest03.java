package com.geekymv.netty.sample.nio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioTest03 {

    public void writeFile(String file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        FileChannel channel = fos.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        for(int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte) i);
        }

        buffer.flip();

        channel.write(buffer);

        fos.close();
    }

}
