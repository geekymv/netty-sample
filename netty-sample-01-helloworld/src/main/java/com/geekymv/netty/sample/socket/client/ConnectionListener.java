//package com.geekymv.netty.sample.socket.client;
//
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelFutureListener;
//import io.netty.channel.EventLoop;
//
//import java.util.concurrent.TimeUnit;
//
//public class ConnectionListener implements ChannelFutureListener {
//
//
//    @Override
//    public void operationComplete(ChannelFuture future) throws Exception {
//        if(!future.isSuccess()) {
//            EventLoop loop = future.channel().eventLoop();
//            loop.schedule(()-> {
//                System.out.println("re connect...");
//                Client.getInstance().connect();
//
//            }, 1L, TimeUnit.SECONDS);
//        }else {
//            System.out.println("connect success...");
//        }
//
//    }
//}
