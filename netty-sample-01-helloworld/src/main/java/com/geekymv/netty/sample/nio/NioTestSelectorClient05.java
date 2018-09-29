package com.geekymv.netty.sample.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioTestSelectorClient05 {

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();

        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_CONNECT);

        channel.connect(new InetSocketAddress("127.0.0.1", 8899));

        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectionKeys.iterator();
            while (iter.hasNext()) {
                SelectionKey selectionKey = iter.next();
                iter.remove();

                if(selectionKey.isConnectable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    socketChannel.finishConnect();

                    System.out.println("connect success");

                    // 注册读事件
                    socketChannel.register(selector, SelectionKey.OP_READ);

                    ByteBuffer line = ByteBuffer.allocate(128);
                    line.put("hello server".getBytes());
                    line.flip();
                    socketChannel.write(line);

                    input(socketChannel);

                }else if(selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                    ByteBuffer buffer = ByteBuffer.allocate(512);
                    int len = socketChannel.read(buffer);
                    if(len > 0) {
                        System.out.println("从服务端发送的消息 = " + new String(buffer.array(), 0, len));
                    }

                }
            }
        }
    }


    /**
     * 从控制台输入数据，并发送出去
     * @param channel
     */
    private static void input( SocketChannel channel) {
        new Thread(()-> {
            ByteBuffer buffer =  ByteBuffer.allocate(512);
            while (true) {
                try {
                    buffer.clear();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    String line = reader.readLine();

                    buffer.put(line.getBytes());
                    buffer.flip();
                    channel.write(buffer);

                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
