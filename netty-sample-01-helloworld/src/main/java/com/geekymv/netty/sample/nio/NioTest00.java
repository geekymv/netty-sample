package com.geekymv.netty.sample.nio;

import java.io.FileInputStream;

public class NioTest00 {

    public static void main(String[] args) throws Exception {

        FileInputStream fis = new FileInputStream("");

        byte[] buffer = new byte[1024];

        fis.read();


    }

}
