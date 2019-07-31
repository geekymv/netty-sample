package com.geekymv.netty.sample.socket.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Client {

    public static final Client client = new Client();

//    private final Bootstrap bootstrap;

    private Client(){
        connect();
    }

    public static Client getInstance() {
        return client;
    }

    private Channel channel;

    private CountDownLatch latch = new CountDownLatch(1);

    public Channel getChannel() {
        try {
            // 等待10s 防止第一次发送数据还没连接成功
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return channel;
    }

    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

//    private static final ClientHandler clientHandler = new ClientHandler();


    public void connect() {
        if(channel != null) {
            channel.close();
        }
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new IdleStateHandler(0, 0, 60, TimeUnit.SECONDS));
                        pipeline.addLast(new ClientIdleHandler());
                        pipeline.addLast(new LineBasedFrameDecoder(1024));
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new ClientHandler());
                    }
                });
        try {
            ChannelFuture channelFuture = bootstrap.connect("localhost", 9876)
                    .addListener((ChannelFuture future)-> {
                        if(!future.isSuccess()) {
                            future.channel().close();
                            EventLoop loop = future.channel().eventLoop();
                            loop.schedule(()-> {
                                System.out.println("bootstrap re connect...");
//                                Client.getInstance().connect();

                            }, 3L, TimeUnit.SECONDS);
                        }else {
                            System.out.println("connect success...");
                            Client.this.channel = future.channel();
                            latch.countDown();
                        }
                    });



        }catch (Exception e) {
            System.out.println("client error = " + e.getClass());
            // 重连
            if(channel != null && channel.isActive()) {
                channel.close();
            }
            Client.getInstance().connect();
        }
    }



}
