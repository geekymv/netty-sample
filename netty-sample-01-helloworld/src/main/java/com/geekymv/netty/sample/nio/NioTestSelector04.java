package com.geekymv.netty.sample.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioTestSelector04 {

    public static void main(String[] args) throws IOException {
        // 创建一个选择器
        Selector selector = Selector.open();

        // 打开服务端套接字通道
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 设置此通道为非阻塞模式
        ssc.configureBlocking(false);

        // 绑定端口
        ssc.bind(new InetSocketAddress(8899));

//        ServerSocket ss = ssc.socket();
//        ss.bind(new InetSocketAddress(8899));

        // 向给定的选择器注册此通道的接受连接事件
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("Server started");
        
        while (true) {
            // select()方法会阻塞，直到至少有一个已注册的事件发生。
            int num = selector.select();
            System.out.println("num = " + num);

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectionKeys.iterator();

            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                // 删除处理过的key
                iter.remove();

                if(key.isAcceptable()) {
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                    // 接受新的连接
                    SocketChannel sc = serverSocketChannel.accept();
                    sc.configureBlocking(false);

                    sc.register(selector, SelectionKey.OP_READ);
                    System.out.println("Got connection from " + sc);

                }else if (key.isReadable()) {
                    SocketChannel sc = (SocketChannel) key.channel();

                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    while (true) {
                        buffer.clear();
                        int read = 0;
                        try {
                            read = sc.read(buffer);
                        }catch (IOException ex) {
                            sc.close();
                            System.out.println("读取发生异常 = " + ex);
                            break;
                        }
                        if(read == 0) {
                            break;
                        }
                        if(read == -1) {
                            /**
                             * 关闭通道
                             * https://blog.csdn.net/u010900754/article/details/78382629
                             */
                            sc.close();
                            break;
                        }

                        System.out.println("客户端发送的消息 = " + new String(buffer.array(), 0, read));

                        buffer.flip();
                        sc.write(buffer);
                    }

                }
            }
        }
    }

}
