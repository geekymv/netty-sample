package com.geekymv.netty.sample.protobuf.server;

import com.geekymv.netty.sample.protobuf.OrderInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ProtoBufServerHandler extends SimpleChannelInboundHandler<OrderInfo.Order> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, OrderInfo.Order msg) throws Exception {
        System.out.println(msg);
    }
}
