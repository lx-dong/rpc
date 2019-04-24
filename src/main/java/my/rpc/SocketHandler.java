package my.rpc;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SocketHandler implements Runnable{
    private Socket socket;
    private ExecutorService pool = Executors.newCachedThreadPool();
    private JavaBeanInputStreamHandler inputStreamHandler;
    private JavaBeanOutputStreamHandler outputStreamHandler;

    public SocketHandler(Socket socket) throws IOException {
        System.out.println("==== SocketHandler init..");
        this.socket = socket;
        System.out.println("--- inputStreamHandler init..");
        this.inputStreamHandler = new JavaBeanInputStreamHandler(socket.getInputStream());
        System.out.println("--- outputStreamHandler init..");
        this.outputStreamHandler = new JavaBeanOutputStreamHandler(socket.getOutputStream());
        System.out.println("==== SocketHandler init end.");
    }

    @Override
    public void run() {
        try {
            System.out.println("==== SocketHandler run..");

            while (true) {
                if (inputStreamHandler.available()) {
                    System.out.println("--- inputStreamHandler available, try read request..");
                    Request request = inputStreamHandler.readRequest();
                    System.out.println("--- read request:" + request);
                    pool.execute(() -> {
                        try {
                            System.out.println("--- invoke request..");
                            Response response = invoke(request);
                            System.out.println("--- return response: " + response + ", write output..");
                            outputStreamHandler.writeResponse(response);
                            System.out.println("--- write over.");
                        } catch (IOException e) {
                            e.printStackTrace();
                            try {
                                System.out.println("IOException, socket close..");
                                socket.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                System.out.println("IOException, socket close..");
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private Response invoke(Request request){
        DefaultResponse response = new DefaultResponse(request.getRequestId());
        try {
            Object service = ServiceRegisterHolder.getInstance().get(SocketServiceRegister.class).getService(request.getClassName());
            if (service == null) {
                System.out.println("service " + request.getClassName() + " not found!");
            }
            Object[] params = request.getParams();
            Class<?>[] types = request.getParamTypes();
            // invoke
            Method m = service.getClass().getMethod(request.getMethodName(), types);
            Object result = m.invoke(service, params);
            response.setData(result);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setData(new RpcException("service error: " + e.getMessage()));
            return response;
        }
    }

}
