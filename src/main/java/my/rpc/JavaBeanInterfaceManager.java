package my.rpc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class JavaBeanInterfaceManager {
    class BeanWrapper {
        private boolean singleton;
        private Class clazz;
        private Object bean;

        public BeanWrapper(boolean singleton, Class clazz, Object bean) {
            this.singleton = singleton;
            this.clazz = clazz;
            this.bean = bean;
        }

        public boolean isSingleton() {
            return singleton;
        }

        public void setSingleton(boolean singleton) {
            this.singleton = singleton;
        }

        public Object getBean() {
            return bean;
        }

        public void setBean(Object bean) {
            this.bean = bean;
        }

        public Class getClazz() {
            return clazz;
        }

        public void setClazz(Class clazz) {
            this.clazz = clazz;
        }
    }
    private Map<Class, BeanWrapper> beanMap = new ConcurrentHashMap<>();

    private JavaBeanInterfaceManager(){

    }

    public static JavaBeanInterfaceManager getInstance() {
        return innerInstanceHolder.instance;
    }
    private static class innerInstanceHolder{
        private final static JavaBeanInterfaceManager instance = new JavaBeanInterfaceManager();
    }

    public <T> T addSingle(Class<? super T> interfaceClass, Class<T> implClass) throws IllegalAccessException, InstantiationException {
        BeanWrapper wrapper = beanMap.get(interfaceClass);
        if (wrapper == null) {
            T instance = implClass.newInstance();
            beanMap.putIfAbsent(interfaceClass, new BeanWrapper(true, implClass, instance));
            return instance;
        }
        else {
            if (!implClass.equals(wrapper.getClazz())) {
                throw new IllegalArgumentException("param impl class=" + implClass.getName() + ", but we already have impl class=" + wrapper.getClazz().getName());
            }
            if (wrapper.isSingleton()) {
                return (T)wrapper.getBean();
            } else {
                throw new IllegalArgumentException("impl is already exit, and it is prototype!");
            }
        }
    }

    public <T> void addPrototype(Class<? super T> interfaceClass, Class<T> implClass) {
        BeanWrapper wrapper = beanMap.get(interfaceClass);
        if (wrapper == null) {
            beanMap.putIfAbsent(interfaceClass, new BeanWrapper(false, implClass, null));
        } else {
            if (!implClass.equals(wrapper.getClazz())) {
                throw new IllegalArgumentException("param impl class=" + implClass.getName() + ", but we already have impl class=" + wrapper.getClazz().getName());
            }
            if (wrapper.isSingleton()) {
                throw new IllegalArgumentException("impl is already exit, and it is singleton!");
            }
        }
    }

    public <T> T getBean(Class<? super T> interfaceClass) throws IllegalAccessException, InstantiationException {
        BeanWrapper wrapper = beanMap.get(interfaceClass);
        if (wrapper != null) {
            if (wrapper.isSingleton()) {
                return (T)wrapper.getBean();
            } else {
                return (T) wrapper.getClazz().newInstance();
            }
        } else {
            return null;
        }
    }

    public <T> T getBean(String interfaceName) throws IllegalAccessException, InstantiationException {
        try {
            Class interfaceClass = Class.forName(interfaceName);
            return (T) getBean(interfaceClass);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

}
