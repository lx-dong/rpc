package my.rpc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceRegisterHolder {
    private Map<Class<? extends ServiceRegister>, ServiceRegister> cache;

    private ServiceRegisterHolder() {
        cache = new ConcurrentHashMap();
    }

    private static class InnerSingletonHolder{
        private static final ServiceRegisterHolder instance = new ServiceRegisterHolder();
    }

    public static ServiceRegisterHolder getInstance() {
        return InnerSingletonHolder.instance;
    }

    public void hold(ServiceRegister register) {
        cache.putIfAbsent(register.getClass(), register);
    }

    public <T extends ServiceRegister> T get(Class<T> type) {
        ServiceRegister result = cache.get(type);
        if (result == null) {
            return null;
        }
        return (T) result;

    }
}
