package com.geekymv.netty.sample.time.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class TimeServerHandler2 extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        int cnt = msg.refCnt();
        System.out.println("handler2 cnt = " + cnt);

//        msg.readerIndex(0);

        int len = msg.readableBytes();
        byte[] bytes = new byte[len];
        msg.readBytes(bytes);

        System.out.println("handler2 = " + new String(bytes, CharsetUtil.UTF_8));

    }
}
