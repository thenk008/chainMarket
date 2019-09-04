package com.shareData.chainMarket.classLoader;

import java.lang.reflect.Field;

public class AppClassLoader extends ClassLoader {
	private static class SingletonHolder {
        public final static AppClassLoader instance = new AppClassLoader();
    }

    public static AppClassLoader getInstance() {
        return SingletonHolder.instance;
    }
    public Class<?> findClassByBytes(String className, byte[] classBytes) {
        return defineClass(className, classBytes, 0, classBytes.length);
    }

    private AppClassLoader() {

    }
    public Object getObj(Class<?> clazz, Object srcObj) {
        try {
            Object newInstance = clazz.getDeclaredConstructor().newInstance();
            Field[] fields = srcObj.getClass().getDeclaredFields();
            for (Field oldInstanceField : fields) {
                String fieldName = oldInstanceField.getName();
                oldInstanceField.setAccessible(true);
                Field newInstanceField = newInstance.getClass().getDeclaredField(fieldName);
                newInstanceField.setAccessible(true);
                newInstanceField.set(newInstance, oldInstanceField.get(srcObj));
            }
            return newInstance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
