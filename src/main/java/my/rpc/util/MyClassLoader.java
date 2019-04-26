package my.rpc.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by lx-dong on 2019/4/26.
 * 加载外部class
 */
public class MyClassLoader extends ClassLoader{
    private String root;

    public void setRoot(String root) {
        this.root = root;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException { // 重载
        byte[] classData = loadClassData(name);
        if (classData == null) {
            throw new ClassNotFoundException();
        }
        return defineClass(name, classData, 0, classData.length);
    }

    private byte[] loadClassData(String className) {
        String fileName = root + File.separator + className.replace('.', File.separatorChar) + ".class"; // replace 是全替换，replaceAll是正则替换，
                                                                                                                    // '.' 会被认为是通配符，split也是正则匹配
        InputStream is = null;

        try {
            is = new FileInputStream(fileName);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = 0;
            while ((length = is.read(buffer)) != -1) {
                bos.write(buffer, 0, length); // 不能全量write(buffer), 因为全量替换最后一次buffer如果不满会有脏数据插入。
            }
            return bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } // try

    }


    public static void main(String[] args) {
        MyClassLoader classLoader = new MyClassLoader();
        classLoader.setRoot("F:/test");
        try {
            Class<?> clazz = classLoader.loadClass("com.sj.analysisplugin.util.MathUtils");
            Object instance = clazz.newInstance();
            Method[] methods = clazz.getMethods();
            System.out.println(methods.length);
            System.out.println(instance.getClass());

            Method method = clazz.getMethod("roundHalfUp", double.class, int.class);
            double result = (double)method.invoke(instance, 1.56, 1);
            System.out.println(result);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


}
