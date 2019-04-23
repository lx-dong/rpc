package my.test;

import my.rpc.client.RpcProxyClient;
import my.rpc.javabean.IHello;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RpcClientTest {

    public static void main(String[] args) {
        RpcProxyClient client = null;
        try {
            client = new RpcProxyClient("localhost", 9999);
        } catch (IOException e) {
            e.printStackTrace();
        }
        IHello proxyClient = client.proxyClient(IHello.class);

//        String result = proxyClient.sayHi("1");
//        System.out.println("result = " + result);
        ExecutorService pool = Executors.newCachedThreadPool();

        for (int i = 0; i < 20; i ++) {
            final int index = i;
            pool.execute(() -> {
                try {
                    String result = proxyClient.sayHi(String.valueOf(index));
                    System.out.println("result = " + result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

}
