package my.rpc.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import my.rpc.Request;
import my.rpc.ServiceRegister;
import my.rpc.constant.CommonConstant;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lx-dong on 2019/4/26.
 */
public class NettyServiceHandler extends SimpleChannelInboundHandler<Request> {
    private ServiceRegister register;
    private ExecutorService pool = new ThreadPoolExecutor(CommonConstant.coreThreadSize, CommonConstant.maxThreadSize, 60, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(), new NamedThreadFactory("NettyServiceHandler"));

    public NettyServiceHandler(ServiceRegister register) {
        super();
        this.register = register;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Request request) throws Exception {
        Channel channel = ctx.channel();
        // call
        // 1,获取请求对应服务接口
        // 2,执行请求函数，返回结果
        // 3,封装为response，序列化为byte[]
        // 4,channel.write  输出响应流

        register.

    }


    static class NamedThreadFactory implements ThreadFactory{ // 名字不能重复，默认不会重复

        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;
        private static boolean daemon;

        NamedThreadFactory() {
            this(String.valueOf(poolNumber.getAndIncrement()), false);
        }

        NamedThreadFactory(String prefix) {
            this(prefix, false);
        }

        NamedThreadFactory(String prefix, boolean daemon) {

            SecurityManager s = System.getSecurityManager();
            this.group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            this.namePrefix = "pool-" +
                    prefix +
                    "-thread-";
            this.daemon = daemon;
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
                t.setDaemon(daemon);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
}
