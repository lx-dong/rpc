package my.test;

import my.rpc.client.RpcProxyClient;
import my.rpc.javabean.IHello;

import java.io.IOException;

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

        for (int i = 0; i < 5; i ++) {
            try {
                String result = proxyClient.sayHi(String.valueOf(i));
                System.out.println("result = " + result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
