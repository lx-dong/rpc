package my.rpc;

public interface ServiceRegister {
    <T> void register(Class<? super T> serviceInterface, Class<T> impl);

    Object getService(String serviceName);
}
