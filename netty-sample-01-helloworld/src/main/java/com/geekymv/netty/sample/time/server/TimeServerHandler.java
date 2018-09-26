package com.geekymv.netty.sample.time.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();

        ByteBuf buf = (ByteBuf) msg;
        int len = buf.readableBytes();
        byte[] bytes = new byte[len];
//        buf.readBytes(bytes);
        buf.getBytes(buf.readerIndex(), bytes);

        String content = new String(bytes, CharsetUtil.UTF_8);
        System.out.println("handler1 = " + content);

        int cnt = buf.refCnt();
        System.out.println("handler1 cnt = " + cnt);

//        ctx.fireChannelRead(msg);

        channelGroup.forEach((ch)->{
            if(channel == ch) {
                ch.writeAndFlush(Unpooled.copiedBuffer("我：" + content + "\r\n", CharsetUtil.UTF_8));
            }else {
                ch.writeAndFlush(Unpooled.copiedBuffer(channel.remoteAddress() + "：" + content + "\r\n", CharsetUtil.UTF_8));
            }
        });

    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("handler add");
        System.out.println("channel is acvive " + channel.isActive());
        System.out.println("add " + channel.remoteAddress());

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("hander removed");

        channelGroup.writeAndFlush(Unpooled.copiedBuffer(channel.remoteAddress() + " is shut down", CharsetUtil.UTF_8));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("channel active");

        channelGroup.add(channel);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("channel inactive");
    }

}
