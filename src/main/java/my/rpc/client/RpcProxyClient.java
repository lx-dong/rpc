package my.rpc.client;

import my.rpc.*;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class RpcProxyClient {
    private final String host;
    private final int port;
    private Socket socket;
    private JavaBeanInputStreamHandler inputStreamHandler;
    private JavaBeanOutputStreamHandler outputStreamHandler;
    private ClientContextHolder contextHolder;
    private AtomicLong counter = new AtomicLong(1);

    private volatile boolean started = false;

    private ExecutorService pool;

    public RpcProxyClient(final String host, final int port) throws IOException {
        this.host = host;
        this.port = port;
        this.socket = ClientSocketHolder.getInstance().getOrBuild(host, port);
    }

    private synchronized void start() throws IOException {
        if (started) return;

        this.outputStreamHandler = new JavaBeanOutputStreamHandler(socket.getOutputStream());
        this.inputStreamHandler = new JavaBeanInputStreamHandler(socket.getInputStream());
        this.contextHolder = new ClientContextHolder();
        this.pool = Executors.newCachedThreadPool();

        new Thread(() -> {
            try {
                while (inputStreamHandler.available()) {
                    System.out.println("input stream available..");
                    Response response = inputStreamHandler.readResponse();
                    ClientContext context = contextHolder.remove(response.getRequestId());
                    if (context == null) {
                        System.out.println("response not match any request, contextId=" + response.getRequestId());
                    } else {
                        context.setResponse(response);
                        context.notifyAll();
                        System.out.println("input stream read response end.");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                inputStreamHandler.shutdown();
            }
        }).start();
    }

    public <T> T proxyClient(Class<T> clazz) {
        final String serviceName = clazz.getName();

        return clazz.cast(Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{
                        try {
                            if (!started) {
                                start();
                            }
                            if (socket.isConnected()) {
                                System.out.println("--- connect success");
                            } else {
                                throw new RuntimeException("connect fail!");
                            }
                            Request request = new DefaultRequest(newRequestId(), serviceName, method.getName(), args);
                            ClientContext context = new ClientContext(request);
                            contextHolder.add(context);

                            System.out.println("request start: " + request.toString());
                            outputStreamHandler.writeRequest(request);

                            synchronized (context) {
                                try {
                                    context.wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                    throw new RuntimeException(e);
                                }
                            }

                            Response response = context.getResponse();
                            System.out.println("get response: " + response);
                            return response;
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw e;
                        }
                    } // invoke
                }
        ));
    }

    private Long newRequestId() {
        return counter.getAndIncrement();
    }
}
