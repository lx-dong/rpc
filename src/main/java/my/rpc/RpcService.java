package my.rpc;

public interface RpcService {
    void start();

    String getStatus();

    void shutdown();

    public <T> void register(Class<? super T> serviceInterface, Class<T> impl);
}
