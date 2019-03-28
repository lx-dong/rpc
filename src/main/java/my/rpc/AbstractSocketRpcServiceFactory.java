package my.rpc;

public abstract class AbstractSocketRpcServiceFactory implements RpcServiceFactory {

    abstract RpcService build(SocketRpcServiceConfig config);

}
