package my.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class JavaBeanSocketRpcService implements RpcService {
    String name;
    SocketRpcServiceConfig config;
    ServerSocket serverSocket;
    Map<String, Object> registerMap = new HashMap<>();
    volatile Thread socketMonitor = null;
    volatile Status status = Status.READY;

    enum Status{
        READY, CONNECTING, RUNNING
    }

    public JavaBeanSocketRpcService(SocketRpcServiceConfig config) {
        this.config = config;
        this.name = config.getName();
    }

    public <T> void register(Class<? super T> serviceInterface, Class<T> impl) {
        try {
            T implInstance = JavaBeanInterfaceManager.getInstance().addSingle(serviceInterface, impl);
            registerMap.putIfAbsent(serviceInterface.getName(), implInstance);
        } catch (Exception e) {
            throw new RuntimeException("register fail!", e);
        }
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
                status = Status.CONNECTING;
                serverSocket = new ServerSocket(config.getPort());
                System.out.println("---open port");
                status = Status.RUNNING;
                while (true) {
                    try (Socket socket = serverSocket.accept()) {
                        System.out.println("---socket connect");
                        try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
                            String serviceName = ois.readUTF();
                            System.out.println("---serviceName=" + serviceName);
                            String methodName = ois.readUTF();
                            System.out.println("---methodName=" + methodName);
                            Object[] args = (Object[]) ois.readObject();
                            System.out.println("---args=" + Arrays.toString(args));
                            Class<?>[] types = new Class[args.length];
                            for (int i = 0; i < args.length; i ++) {
                                types[i] = args[i].getClass();
                            }
                            System.out.println("---types=" + Arrays.toString(types));

                            Object service = getService(serviceName);
                            if (service == null) {
                                System.out.println("service " + serviceName + " not found!");
                            }
                            // invoke
                            Method m = service.getClass().getMethod(methodName, types);
                            Object result = m.invoke(service, args);
                            System.out.println("---result=" + result);
                            // back result
                            try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
                                oos.writeObject(result);
                                oos.flush();
                            }
                            System.out.println("--- back result over");
                        }
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

    }

    public Object getService(String serviceName) {
        return registerMap.get(serviceName);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
