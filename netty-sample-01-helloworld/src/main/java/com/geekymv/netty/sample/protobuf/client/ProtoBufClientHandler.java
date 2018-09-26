package com.geekymv.netty.sample.protobuf.client;

import com.geekymv.netty.sample.protobuf.OrderInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ProtoBufClientHandler extends SimpleChannelInboundHandler<OrderInfo.Order> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client channel active");

        OrderInfo.Order order = OrderInfo.Order.newBuilder()
                .setQuery("query1")
                .setPageNumber(1)
                .setResultPerPage(2)
                .build();

        ctx.writeAndFlush(order);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, OrderInfo.Order msg) throws Exception {

    }
}
