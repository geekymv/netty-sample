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
- 创建两个NioEventLoopGroup 实例，bossGroup 用于服务端接受客户端的连接，workerGroup 用于进行客户端连接SocketChannel 的网络读写,
- 创建ServerBootstrap 的实例，ServerBootstrap是启动服务端的辅助类，目的是降低服务端的开发复杂度；
- 调用ServerBootstrap的group 方法，将两个线程组实例当作参数传递到ServerBootstrap中；
- 设置创建的Channel类型为NioServerSocketChannel，它的功能主要是负责创建子Channel，这些子Channel代表已接受的连接，
这里的channel()参数类型为Class，内部是通过反射来实现Channel的创建，内部持有java.nio.channels.ServerSocketChannel的引用；
- 绑定I/O事件的处理类childHandler，主要用于处理网络IO事件，例如对消息编解码、业务处理等；
- 调用bind 方法绑定监听端口，链式调动同步阻塞方法sync 等待绑定操作完成，返回的ChannelFuture用于异步操作的通知回调；
- 使用future.channel().closeFuture().sync(); 进行阻塞，直到服务端Channel关闭；
- finally{} 中进行优雅退出，释放相关资源。


客户端创建步骤：

- 创建客户端处理I/O 读写的NioEventLoopGroup 线程组；
- 创建客户端辅助启动类Bootstrap 实例；
- Channel的配置与服务端不同的是需要设置为NioSocketChannel；
- 添加handler，使用匿名内部类ChannelInitializer，实现它的抽象方法initChannel，
向pipline中添加我们自己的业务处理handler；
- 调用connect 方法发起异步连接，调用同步方法等待连接成功；
- 其他跟服务端类似。


#### Netty的核心组件介绍及原理分析

- Channel
Channel 是一个Java NIO的基本构造，可以把Channel类比一个Socket。

- EventLoop
EventLoop 本身只有一个线程驱动（Thread），其处理了一个Channel 的所有I/O事件，并且在该EventLoop的整个生命周期内都不会改变。
单个EventLoop可能会被指派用于服务多个Channel。
我们看下NioEventLoop 的类层次结构：
// TODO 此处有图

在这个模型中，一个EventLoop 将由一个永远不会改变的Thread 驱动，同时任务(Runnable 或者Callable)可以直接提交给EventLoop，任务
将被放入Queue中，会被与其关联的Thread执行。
下面是EventLoop的父类SingleThreadEventLoop的父类SingleThreadEventExecutor 中的核心代码：
```text
@Override
public void execute(Runnable task) {
    if (task == null) {
        throw new NullPointerException("task");
    }

    boolean inEventLoop = inEventLoop();
    if (inEventLoop) {
        addTask(task);
    } else {
        startThread();
        addTask(task);
        if (isShutdown() && removeTask(task)) {
            reject();
        }
    }

    if (!addTaskWakesUp && wakesUpForTask(task)) {
        wakeup(inEventLoop);
    }
}

```

- EventLoopGroup
NioEventLoopGroup 拥有一个或多个EventLoop，具体个数可以通过构造方法指定，一般我们可以指定bossGroup线程组的线程个数为1，
workerGroup使用框架提供的默认个数或者自己指定，通过跟踪NioEventLoopGroup源代码可以发现默认个数为Runtime.getRuntime().availableProcessors() * 2。
EventLoopGroup 负责为每个新创建的Channel 分配一个EventLoop，通过EventExecutorChooser.next()方法(使用了round-robin算法)
进行分配以获取一个均匀的分布，并且同一个EventLoop可能会被分配给多个Channel。NioEventLoopGroup 内部是NioEventLoop。


- ChannelHandler
Netty是事件驱动的网络编程框架，Netty 使用不同的事件来通知我们状态的改变或者是操作的状态。
这使得我们可以基于已发生的事件来触发适当的动作，
Netty 定义了两个最重要的ChannelHandler 子接口：
- ChannelInboundHandler 处理入站数据以及各种状态变化；
- ChannelOutboundHandler 处理出站数据并且允许拦截。

- ChannelPipeline
每一个新创建的Channel 都将会被分配一个新的ChannelPipeline，并且这项关联是永久性的，Channel即不能附加另一个ChannelPipeline，也不能分离当前的。
因为AbstractChannel持有一个DefaultChannelPipeline的引用，private final DefaultChannelPipeline pipeline; 在Channel初始化时被赋值，
其中DefaultChannelPipeline中也有一个Channel的引用。这也就是说我们可以通过Channel 获取其关联的ChannelPipeline，也可以通过ChannelPipeline
获取其关联的Channel，两者是一对一的关联关系。

```text
/**
 * Creates a new instance.
 *
 * @param parent
 *        the parent of this channel. {@code null} if there's no parent.
 */
protected AbstractChannel(Channel parent) {
    this.parent = parent;
    id = newId();
    unsafe = newUnsafe();
    pipeline = newChannelPipeline();
}

/**
 * Returns a new {@link DefaultChannelPipeline} instance.
 */
protected DefaultChannelPipeline newChannelPipeline() {
    return new DefaultChannelPipeline(this);
}

```

- ChannelHandlerContext
ChannelHandlerContext 代表了ChannelHandler 和ChannelPipeline 之间的关联，每当有ChannelHandler 添加到ChannelPipeline中时，
都会创建ChannelHandlerContext，ChannelHandlerContext 的主要功能是管理它所关联的ChannelHandler 和在同一个ChannelPipeline中的其他
ChannelHandler 之间的交互。

ChannelHandlerContext事件流
此处有图 p84

- ServerBootstrapConfig
代表ServerBootstrap的配置信息


#### 究竟EventLoopGroup 是如何为Channel 分配一个EventLoop的呢？

bind() --> doBind() --> initAndRegister()中
ChannelFuture regFuture = config().group().register(channel);

NioEventLoopGroup的父类MultithreadEventLoopGroup 中的注册方法
```text
@Override
public ChannelFuture register(Channel channel) {
    return next().register(channel);
}
```

next()方法返回一个通过EventExecutorChooser.next()方法返回的EventLoop

NioEventLoop 的父类SingleThreadEventLoop
```text
@Override
public ChannelFuture register(Channel channel) {
    return register(new DefaultChannelPromise(channel, this));
}

@Override
public ChannelFuture register(final ChannelPromise promise) {
    ObjectUtil.checkNotNull(promise, "promise");
    promise.channel().unsafe().register(this, promise);
    return promise;
}
```
将调用AbstractChannel 中的内部类AbstractUnsafe的register方法
```text
@Override
public final void register(EventLoop eventLoop, final ChannelPromise promise) {
    if (eventLoop == null) {
        throw new NullPointerException("eventLoop");
    }
    if (isRegistered()) {
        promise.setFailure(new IllegalStateException("registered to an event loop already"));
        return;
    }
    if (!isCompatible(eventLoop)) {
        promise.setFailure(
                new IllegalStateException("incompatible event loop type: " + eventLoop.getClass().getName()));
        return;
    }

    AbstractChannel.this.eventLoop = eventLoop;

    if (eventLoop.inEventLoop()) {
        register0(promise);
    } else {
        try {
            eventLoop.execute(new Runnable() {
                @Override
                public void run() {
                    register0(promise);
                }
            });
        } catch (Throwable t) {
            logger.warn(
                    "Force-closing a channel whose registration task was not accepted by an event loop: {}",
                    AbstractChannel.this, t);
            closeForcibly();
            closeFuture.setClosed();
            safeSetFailure(promise, t);
        }
    }
}

```
这段代码AbstractChannel.this.eventLoop = eventLoop;将eventLoop和Channel进行绑定。