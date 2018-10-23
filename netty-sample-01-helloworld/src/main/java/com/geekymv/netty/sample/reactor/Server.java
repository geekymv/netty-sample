package com.geekymv.netty.sample.reactor;

import java.io.IOException;

public class Server {

    public static void main(String[] args) throws IOException {

        Reactor reactor = new Reactor(8899);

        new Thread(reactor).start();

    }

}
