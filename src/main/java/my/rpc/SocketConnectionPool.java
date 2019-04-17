package my.rpc;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SocketConnectionPool {
    private ExecutorService pool;
    private AtomicInteger times = new AtomicInteger();


    public SocketConnectionPool() {
        pool = Executors.newCachedThreadPool();

    }

    public void add(SocketHandler handler) {
        pool.execute(handler);
        System.out.println("===socket times=" + times.getAndIncrement());
    }

    public void shutdown() {
        pool.shutdownNow();
    }

}
