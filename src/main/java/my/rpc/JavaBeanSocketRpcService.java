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
        register.register(serviceInterface, impl);
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
                System.out.println("---open port");
                status = Status.RUNNING;
                while (true) {
                    Socket socket = serverSocket.accept();
                    try {
                        pool.add(new SocketHandler(socket));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } // while
            } catch (IOException e) {
                System.out.println("ServiceSocket error!" + e.getMessage());
                e.printStackTrace();
                System.out.println("sleep 2s");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                System.out.println("retry..");
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
