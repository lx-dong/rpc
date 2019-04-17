package my.test;

import my.rpc.client.RpcProxyClient;
import my.rpc.javabean.IHello;

public class RpcClientTest {

    public static void main(String[] args) {
        RpcProxyClient client = new RpcProxyClient();
        IHello proxyClient = client.proxyClient(IHello.class, "localhost", 9999);
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
