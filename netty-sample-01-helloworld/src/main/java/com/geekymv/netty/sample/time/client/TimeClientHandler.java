package com.geekymv.netty.sample.time.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * Created by Administrator on 2018/9/11.
 */
public class TimeClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    /**
     * 客户端和服务端连接建立成功后，该方法被调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel active");

        for(int i = 0; i < 100; i++) {
            ctx.writeAndFlush(Unpooled.copiedBuffer("hello server " + i + "\r\n", CharsetUtil.UTF_8));
        }

    }

    /**
     * 当服务端返回应答消息时，该方法被调用
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] bytes = new byte[msg.readableBytes()];
        msg.getBytes(msg.readerIndex(), bytes);

        System.out.println("from server " + new String(bytes, CharsetUtil.UTF_8));
    }
}
