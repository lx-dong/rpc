package my.rpc.netty;

import my.rpc.DefaultProvider;
import my.rpc.Provider;
import my.rpc.ServiceRegister;

import java.util.HashMap;
import java.util.Map;

public class NettyServiceRegister implements ServiceRegister {
    private Map<String, Provider> serviceMap = new HashMap<>();

    @Override
    public <T> void register(Class<? super T> serviceInterface, Class<T> impl) throws Exception{
        synchronized (this) {
            serviceMap.put(serviceInterface.getName(), new DefaultProvider(impl.newInstance()));
        }
    }

    @Override
    public Object getService(String serviceName) {
        return serviceMap.get(serviceName);
    }
}
