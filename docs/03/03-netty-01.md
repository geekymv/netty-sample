下面通过代码演示基于Netty的客户端-服务端通信。
要想在项目中使用Netty，首先我们需要加入Netty的jar包。
Gradle项目中的build.gradle 文件中添加如下内容

```text
dependencies {
    compile (
            'io.netty:netty-all:4.1.30.Final'
    )
}

```
使用Maven的话需要在pom.xml中添加依赖
```text
<dependency>
    <groupId>io.netty</groupId>
    <artifactId>netty-all</artifactId>
    <version>4.1.30.Final</version>
</dependency>

```

编写服务端代码
NettyServer.java

```java
package com.gitchat.netty.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {

    private final int port;

    public NettyServer(int port) {
        this.port = port;
    }

    private void start() throws Exception {
        // 创建两个线程组，其中bossGroup 用于监听端口，接受新的连接。
        //  workerGroup 用于处理传入的客户端连接的数据读写。
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 创建引导类ServerBootstrap，用于引导服务器的启动
            ServerBootstrap b = new ServerBootstrap();
            // 配置线程组，指定线程模型
            b.group(bossGroup, workerGroup)
                    // 指定服务端的IO模型为NIO
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 我们自定义用于处理业务的Handler添加到pipline上
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });

            // 将服务器绑定到指定的端口以监听新的连接请求。同步等待绑定完成。
            ChannelFuture future = b.bind(port).sync();
            System.out.println("NettyServer 已启动，监听端口：" + port);

            //  阻塞，直到Channel 关闭
            future.channel().closeFuture().sync();

        }finally {
            // 关闭EventLoopGroup，释放资源
            bossGroup.shutdownGracefully().sync();
            workerGroup.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {

        NettyServer server = new NettyServer(6789);
        server.start();
    }

}
```
NettyServerHandler.java

```java
package com.gitchat.netty.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.net.InetAddress;

/**
 *
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    /**
     * 连接建立时被调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("接收的连接来自 " + ctx.channel());
        // 发送给客户端的问候
        String welcome = "Welcome to " + InetAddress.getLocalHost().getHostName() + "!";
        ctx.writeAndFlush(Unpooled.copiedBuffer(welcome.getBytes()));
    }

    /**
     * 当客户端有数据发送过来时，channelRead0 方法被调动
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        String body = msg.toString(CharsetUtil.UTF_8);
        System.out.println("客户端发送过来的数据 = " + body);

        String response = "Did you say '" + body +"'?";
        ctx.writeAndFlush(Unpooled.copiedBuffer(response.getBytes()));
    }

    /**
     * 发生异常，打印异常信息，关闭连接
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}

```

客户端代码
```java
package com.gitchat.netty.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {

    private final String host;

    private final int port;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws Exception {
        // 创建线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //
            Bootstrap b = new Bootstrap();
            b.group(group)
                    // 指定客户端的IO模型为NIO
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //
                            pipeline.addLast(new NettyClientHandler());
                        }
                    });

            // 连接到服务器，阻塞等待直到连接完成
            ChannelFuture future = b.connect(host, port).sync();

            // 阻塞，直到Channel 关闭
            future.channel().closeFuture().sync();

        }finally {
            group.shutdownGracefully().sync();
        }

    }

    public static void main(String[] args) throws Exception {

        NettyClient client = new NettyClient("127.0.0.1", 6789);
        client.connect();
    }

}

```
NettyClientHandler.java

```java
package com.gitchat.netty.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class NettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {

        String response = msg.toString(CharsetUtil.UTF_8);

        System.out.println("response = " + response);
    }
}

```


下面我们总结下Netty的服务端和客户端创建基本流程：

服务端创建步骤：
- 创建两个NioEventLoopGroup 实例，bossGroup 用于服务端接受客户端的连接，workerGroup 用于进行客户端连接SocketChannel 的网络读写；
- 创建ServerBootstrap 实例用于启动服务端的辅助类，目的是降低服务端的开发复杂度；
- 调用ServerBootstrap的group 方法，将两个线程组实例当作参数传递到ServerBootstrap中；
- 设置创建的Channel为NioServerSocketChannel，它的功能对应于Java NIO类库中的ServerSocketChannel；
- 绑定I/O事件的处理类childHandler，主要用于处理网络IO事件，例如对消息编解码、业务处理等；
- 调用bind 方法绑定监听端口，链式调动同步阻塞方法sync 等待绑定操作完成，返回的ChannelFuture用于异步操作的通知回调；
- 使用future.channel().closeFuture().sync(); 进行阻塞，直到服务端Channel关闭；
- finally{} 中进行优雅退出，释放相关资源。


客户端创建步骤：

- 

Netty的核心组件

- Channel
Channel 是一个Java NIO的基本构造，可以把Channel类比一个Socket。

- EventLoop
EventLoop 本身只有一个线程驱动，其处理了一个Channel 的所有I/O事件，并且在该EventLoop的整个生命周期内都不会改变。

- ChannelHandler
Netty是事件驱动的网络编程框架，Netty 使用不同的事件来通知我们状态的改变或者是操作的状态。
这使得我们可以基于已发生的事件来触发适当的动作，
