package my.rpc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class JavaBeanSocketRpcService implements RpcService {
    String name;
    SocketRpcServiceConfig config;
    ServerSocket serverSocket;
    ServiceRegister register;
    volatile Thread socketMonitor = null;
    volatile Status status = Status.READY;
    SocketConnectionPool pool;

    enum Status{
        READY, CONNECTING, RUNNING, STOP
    }

    public JavaBeanSocketRpcService(SocketRpcServiceConfig config) {
        this.config = config;
        this.name = config.getName();
        register = new SocketServiceRegister();
        ServiceRegisterHolder.getInstance().hold(register);
        pool = new SocketConnectionPool();
    }

    public <T> void register(Class<? super T> serviceInterface, Class<T> impl) {
        try {
            register.register(serviceInterface, impl);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getPort() {
        return config.getPort();
    }

    public synchronized void start() {
        if (socketMonitor == null) {
            socketMonitor = buildSocketMonitor();
            socketMonitor.setName("JavaBeanSocketRpcService-" + name == null || name.isEmpty() ? "" : name);
            socketMonitor.start();
        }
    }

    private Thread buildSocketMonitor() {
        return new Thread(() -> socketHandle() );
    }

    private void socketHandle() {

        while (true) {
            try {
                if (status == Status.STOP) {
                    return;
                }
                status = Status.CONNECTING;
                serverSocket = new ServerSocket(config.getPort());
                System.out.println("---open port " + config.getPort());
                status = Status.RUNNING;
                while (true) {
                    Socket socket = serverSocket.accept();
                    try {
                        System.out.println("-- new socket come");
                        pool.add(new SocketHandler(socket));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } // while
            } catch (IOException e) {
                System.out.println("ServiceSocket error!" + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public String getStatus() {
        return status.name();
    }

    public void shutdown() {
        try {
            status = Status.STOP;
            serverSocket.close();
            pool.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object getService(String serviceName) {
        return register.getService(serviceName);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
