package my.rpc;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Arrays;

public class SocketHandler implements Runnable{
    private Socket socket;

    public SocketHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
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

                Object service = ServiceRegisterHolder.getInstance().get(SocketServiceRegister.class).getService(serviceName);
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
    }
}
