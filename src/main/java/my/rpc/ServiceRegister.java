package my.rpc;

public interface ServiceRegister {
    <T> void register(Class<? super T> serviceInterface, Class<T> impl) throws Exception;

    Object getService(String serviceName);
}
