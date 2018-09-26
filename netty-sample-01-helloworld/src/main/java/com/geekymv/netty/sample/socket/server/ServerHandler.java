package com.geekymv.netty.sample.socket.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetSocketAddress;

/**
 * Created by Administrator on 2018/9/11.
 */
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("第一个 server handler 接收到数据 = " + msg);

        InetSocketAddress socketAddress = (InetSocketAddress)ctx.channel().remoteAddress();
        String hostName = socketAddress.getHostName();
        int port = socketAddress.getPort();
        System.out.println("客户端信息 host = " + hostName + ", port = " + port);

        ctx.fireChannelRead(msg);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
    }
}
