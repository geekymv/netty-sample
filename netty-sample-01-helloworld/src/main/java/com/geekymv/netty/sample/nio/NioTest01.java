package com.geekymv.netty.sample.nio;

import java.nio.IntBuffer;

public class NioTest01 {

    public static void main(String[] args) {

        IntBuffer buffer = IntBuffer.allocate(10);

        int capacity = buffer.capacity();
        for(int i = 0; i < capacity; i++) {
            buffer.put(i);
            System.out.println("1-------- pos = " + buffer.position() +", cap = " + capacity + ", lim = " + buffer.limit() + ", mark = " + buffer.mark());
        }

        buffer.flip();
        System.out.println("2-------- pos = " + buffer.position() +", cap = " + capacity + ", lim = " + buffer.limit()  + ", mark = " + buffer.mark());

        for(int i = 0; i < capacity; i++) {
            System.out.println(buffer.get());
            System.out.println("3-------- pos = " + buffer.position() +", cap = " + capacity + ", lim = " + buffer.limit()  + ", mark = " + buffer.mark());
        }

    }

}
