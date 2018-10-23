package com.geekymv.netty.sample.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public final class Handler implements Runnable {

    private SocketChannel socket;

    final SelectionKey sk;

    ByteBuffer input = ByteBuffer.allocate(1024);
    ByteBuffer output = ByteBuffer.allocate(1024);


    static final int READING = 0, SENDING = 1;
    int state = READING;

    public Handler(Selector selector, SocketChannel c) throws IOException{
        this.socket = c;
        c.configureBlocking(false);

        sk = socket.register(selector, 0);
        sk.attach(this);
        sk.interestOps(SelectionKey.OP_READ);

        selector.wakeup();
    }

    boolean inputIsComplete () {
        return false;
    }

    boolean outputIsComplete () {
        return false;
    }

    void process () {
        System.out.println("process...");
    }

    @Override
    public void run() {
        try {
            if(state == READING) {
                read();
            }else if(state == SENDING) {
                send();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    void read() throws IOException {
        int len = socket.read(input);

        input.flip();

        System.out.println(new String(input.array(),0, len));

        if(inputIsComplete()) {
            process();
            state = SENDING;

            sk.interestOps(SelectionKey.OP_WRITE);
        }
    }

    void send() throws IOException {
        socket.write(output);
        if(outputIsComplete()) {
            sk.cancel();
        }
    }
}
