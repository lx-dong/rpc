package my.rpc;

import java.util.HashMap;
import java.util.Map;

public class SocketServiceRegister implements ServiceRegister{
    Map<String, Object> registerMap = new HashMap<>();

    @Override
    public <T> void register(Class<? super T> serviceInterface, Class<T> impl) {
        try {
            T implInstance = JavaBeanInterfaceManager.getInstance().addSingle(serviceInterface, impl);
            registerMap.putIfAbsent(serviceInterface.getName(), implInstance);
        } catch (Exception e) {
            throw new RuntimeException("register fail!", e);
        }
    }

    @Override
    public Object getService(String serviceName) {
        return registerMap.get(serviceName);
    }
}
