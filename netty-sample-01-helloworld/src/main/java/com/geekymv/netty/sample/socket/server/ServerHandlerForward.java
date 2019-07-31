package com.geekymv.netty.sample.socket.server;

import com.geekymv.netty.sample.socket.client.Client;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by Administrator on 2018/9/11.
 */
public class ServerHandlerForward extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("第二个 server handler 接收到信息 = " + msg);

        Channel channel = Client.getInstance().getChannel();
        if(channel == null || !channel.isActive()) {
            System.out.println("channel is not acvive...");
            return;
        }
        channel.writeAndFlush(msg);
    }

}
