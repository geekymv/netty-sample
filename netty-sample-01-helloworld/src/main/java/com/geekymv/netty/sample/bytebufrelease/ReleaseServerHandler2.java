package com.geekymv.netty.sample.bytebufrelease;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ReleaseServerHandler2 extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("hander 2 = " + msg.hashCode());
        ByteBuf buf = (ByteBuf)msg;

//        ByteBuf content = Unpooled.copiedBuffer(buf);
        ctx.writeAndFlush(buf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("handler 2");
        cause.printStackTrace();
        ctx.close();
    }
}
