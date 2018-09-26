package com.geekymv.netty.sample.socket.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class Client {

    public static final Client client = new Client();

    private Client(){
        connect();
    }

    public static Client getInstance() {
        return client;
    }

    private Channel channel;

    public Channel getChannel() {
        return channel;
    }

    public void connect() {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
//                        pipeline.addLast(new LineBasedFrameDecoder(1024));
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new ClientHandler());
                    }
                });

        try {
            ChannelFuture channelFuture = bootstrap.connect("localhost", 9876).sync();
            channelFuture.addListener(new ConnectionListener());

            this.channel = channelFuture.channel();

//            channelFuture.channel().close().sync();
//            System.out.println("client close");

        }catch (Exception e) {
            System.out.println("client error = " + e.getClass());
            // 重连
            if(channel != null && channel.isActive()) {
                channel.close();
            }
            Client.getInstance().connect();
        } finally {
//            eventLoopGroup.shutdownGracefully();
        }
    }



}
