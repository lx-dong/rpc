package my.rpc.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import my.rpc.RpcService;

/**
 * Created by lx-dong on 2019/4/24.
 */
public class NettyServiceImpl implements RpcService {
    private EventLoopGroup parentGroup = new EpollEventLoopGroup(1);
    private EventLoopGroup workGroup = new EpollEventLoopGroup();
    private ServerBootstrap serverBootstrap = new ServerBootstrap();


    @Override
    public void start() {
        serverBootstrap
                .group(parentGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128) // TCP内核有A（握手中），B（完成三次握手）两个队列，建立新的连接时，在第二次握手（ack+1）时将客户端连接放入A队列，第三次握手结束将
                                                              // 客户端连接放入第二个队列，accept 会从B队列取出连接建立socket，backlog即是A，B队列长度之和，超出则新连接会被TCP内核拒绝。
                                                              // 个人理解A，B队列是TCP连接缓存，提供了一个缓冲区，也因为如此，客户端连接是顺序处理。

                .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE) // Nagle算法将小的数据包组成大的数据包后再一次性发出，有延迟，高效利用网络，适用于文件传输。
                                                                      // 设置禁用（NODELAY）则小的数据包将不再等待组装直接发送，提高了实时性，网络压力增大。
                .childOption(ChannelOption.SO_REUSEADDR, Boolean.TRUE) // 重用连接和端口，当一个服务器进程占用了一个端口进行监听，其他进程将不能再对此端口进行监听，否则返回错误。
                                                                       // 设为可重用，则其他进程也可以同时对一个端口进行监听。
                                                                       // 当一个进程非正常退出，端口占用将等待一段时间才会释放，若不设置SO_REUSEADDR为true，则无法正常使用此端口，需要重启的情况比较适用。
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT) // 开启对象池，重用缓冲区
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new NettyEncoder(), new NettyDecoder()); // TODO
                    }
                });
        // TODO 后续操作
    }

    @Override
    public String getStatus() {
        return null;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public <T> void register(Class<? super T> serviceInterface, Class<T> impl) {

    }
}
