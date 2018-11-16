package com.geekymv.netty.sample.nio;

import java.net.InetSocketAddress;

public class NioTest06 {


    public static void main(String[] args) {

        InetSocketAddress address = new InetSocketAddress("node01", 8898);
        String hostName = address.getAddress().getCanonicalHostName();

        System.out.println(hostName);
    }

}
