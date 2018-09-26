package com.geekymv.netty.sample.time.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class TimeClient {


    public static void main(String[] args) {

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        // 2048 单条消息的最大长度
                        pipeline.addLast(new DelimiterBasedFrameDecoder(2048, Delimiters.lineDelimiter()));

//                        pipeline.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0,2));
//                        pipeline.addLast(new LengthFieldPrepender(2));

                        pipeline.addLast(new TimeClientHandler());
                    }
                });

        try {
            ChannelFuture channelFuture = bootstrap.connect("localhost", 7890).sync();

            channelFuture.channel().closeFuture().sync();

        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }

    }

}
