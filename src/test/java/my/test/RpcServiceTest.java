package my.test;

import my.rpc.JavaBeanSocketRpcServiceFactory;
import my.rpc.RpcService;
import my.rpc.SocketRpcServiceConfig;
import my.rpc.javabean.HelloImpl;
import my.rpc.javabean.IHello;

public class RpcServiceTest {

    public static void main(String[] args) {
        JavaBeanSocketRpcServiceFactory factory = new JavaBeanSocketRpcServiceFactory();
        SocketRpcServiceConfig config = new SocketRpcServiceConfig(9999);
        config.setName("test");
        RpcService service = factory.build(config);
        service.register(IHello.class, HelloImpl.class);
        service.start();



    }
}
