package com.geekymv.netty.sample.socket.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/9/11.
 */
public class ClientHandler extends SimpleChannelInboundHandler<String> {

    private String msg;

    public ClientHandler(){
    }

    public ClientHandler(String msg){
        this.msg = msg;
    }

    /**
     * 客户端和服务端连接建立成功后，该方法被调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("hello server2");
    }

    /**
     * 当服务端返回应答消息时，该方法被调用
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("from server = " + msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("掉线了");

        EventLoop loop = ctx.channel().eventLoop();
        loop.schedule(()-> {
            System.out.println("掉线重连re connect...");
            Client.getInstance().connect();

        }, 1L, TimeUnit.SECONDS);

        super.channelInactive(ctx);
    }
}
