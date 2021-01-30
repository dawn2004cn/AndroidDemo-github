package com.noahedu.game;

import java.io.IOException;
import java.lang.reflect.*;

public class DynamicProxy implements InvocationHandler {

    private Object o;

    public <T> T getObject(T t) {
        this.o = t;
        return (T) Proxy.newProxyInstance(
                t.getClass().getClassLoader(),
                t.getClass().getInterfaces(),
                this
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("每调用一次代理类的方法, 都得执行一次本方法, 代理类: " + proxy.getClass() + ", 执行的方法名称: " + method.toString());
        printProxyClass(proxy.getClass());
        return method.invoke(o, args);
    }

    private void printProxyClass(Class proxy) throws IllegalAccessException, IOException {
        Field[] fields = proxy.getDeclaredFields();
        for (int i = 0; i < fields.length; i ++) {
            fields[i].setAccessible(true);
            System.out.println(fields[i].toGenericString() + " 类元素指代的方法名称是: " + fields[i].get(proxy).toString());
        }

        System.out.println("==================如下是代理类对方法的代理, 如果有兴趣, 可以取消注释并阅读反编译之后的源码=============");
        // byte[] clazz = ProxyGenerator.generateProxyClass("$Proxy0", new Class[]{proxy});
        // FileOutputStream fos = new FileOutputStream("ProxyClass.class");
        // fos.write(clazz);
        // fos.close();

        System.out.println("===============================");
        Method[] methods = proxy.getDeclaredMethods();
        for (int i = 0; i < methods.length; i ++) {
            System.out.println(methods[i].toGenericString());
        }
    }

    public static void main(String[] args)
            throws InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {
        DynamicProxy dp = new DynamicProxy();
        //UnmodifiedIterator uf = dp.getObject(new JDK8DefaultIterator());
        //System.out.println(uf.hasNext());
    }
}
