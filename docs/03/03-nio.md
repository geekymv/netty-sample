NIO库是在JDK1.4中引入的，NIO弥补了OIO的不足，它在标准Java代码中提供了高速的、面向块的I/O。
通过定义包含数据的类，以及通过块的形式处理这些数据。

NIO核心组件：
- Buffer
缓冲区Buffer包含一些要写入或者要读出的数据，在面向流的I/O中，可以直接向流对象写入数据或者直接从流对象读取数据。
然而，在NIO库中，所有数据都是用缓冲区处理的。在读取数据时，它是直接读到缓冲区中的，在写入数据时，写入到缓冲区中。任何时候访问
NIO中的数据，都是通过缓冲区进行操作。


- Channel
Channel是一个通道，可以通过它读取和写入数据，Channel和Stream的不同之处在于Channel是双向的，Stream只能在一个方向上移动（一个流必须是
InputStream或者OutputStream的子类），Channel可以用于读、写或者同时用于读写。因为Channel是全双工的，所以它可以比流更好地映射底层操作
系统的API。特别是在UNIX网络编程中，底层操作系统的通道都是全双工的，同时支持读写操作。

- Selector
多路复用器Selector，它是Java NIO编程的基础，Selector提供选择已经就绪的任务的能力。
Selector会不断地轮询注册在其上的Channel，如果某个Channel上有新的连接接入、读或写事件，这个Channel就处于就绪状态，
会被Selector 轮询出来，然后通过SelectionKey 可以获取就绪的Channel 的集合，然后进行I/O操作。

下面我们通过代码演示与上面使用OIO类似的功能。


























