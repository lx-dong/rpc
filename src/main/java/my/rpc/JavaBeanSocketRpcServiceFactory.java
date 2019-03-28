package my.rpc;

public class JavaBeanSocketRpcServiceFactory extends AbstractSocketRpcServiceFactory {

    public RpcService build(SocketRpcServiceConfig config) {
        return new JavaBeanSocketRpcService(config);
    }
}
