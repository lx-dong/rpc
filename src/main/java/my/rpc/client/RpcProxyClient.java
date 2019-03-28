package my.rpc.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class RpcProxyClient {
    public <T> T proxyClient(Class<T> clazz, final String localhost, final int port) {
        final String serviceName = clazz.getName();

        return clazz.cast(Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        try (Socket socket = new Socket(localhost, port)) {
                            System.out.println("--- connect success");
                            try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
                                oos.writeUTF(serviceName);
                                System.out.println("---serviceName=" + serviceName);
                                oos.writeUTF(method.getName());
                                System.out.println("---method=" + method);
                                oos.writeObject(args);
                                System.out.println("---args=" + Arrays.toString(args));
                                oos.flush();
                                System.out.println("---flush");

                                try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
                                    Object result = ois.readObject();
                                    System.out.println("result class=" + result.getClass());
                                    return result;
                                }
                            }
                        } // trt
                    } // invoke
                }
        ));
    }
}
