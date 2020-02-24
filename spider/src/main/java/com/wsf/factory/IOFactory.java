package com.wsf.factory;

import com.wsf.io.IReadFromPool;
import com.wsf.io.IWriteToPool;
import com.wsf.io.impl.ReadFromPoolImpl;
import com.wsf.io.impl.WriteToPoolImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class IOFactory {
    /**
     * 获取一个读取器 读
     *
     * @param bufferSize
     * @return
     */
    public static IReadFromPool getReadConnect(Integer bufferSize) {
        ReadFromPoolImpl readFromPool = new ReadFromPoolImpl(bufferSize);
        return (IReadFromPool) Proxy.newProxyInstance(ReadFromPoolImpl.class.getClassLoader(), ReadFromPoolImpl.class.getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                String name = method.getName();
                if (readFromPool.isClosed()) {
                    if (name.equals("close") || name.equals("isClosed")) {
                        method.invoke(readFromPool, objects);
                    } else {
                        System.out.println("当前流已经关闭!");
                    }
                    return null;
                } else {
                    return method.invoke(readFromPool, objects);
                }
            }
        });
    }

    /**
     * 获取一个读取器 写
     *
     * @return
     */
    public static IWriteToPool getWriteConnect() {
        WriteToPoolImpl writeToPool = new WriteToPoolImpl();
        return (IWriteToPool) Proxy.newProxyInstance(WriteToPoolImpl.class.getClassLoader(), WriteToPoolImpl.class.getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                String name = method.getName();
                //如果流关闭了就不可以再读取了，只能执行close方法
                if (writeToPool.isClosed()) {
                    if (name.equals("close") || name.equals("isClosed")) {
                        return method.invoke(writeToPool, objects);
                    } else {
                        System.out.println("当前流已经关闭!");
                        return null;
                    }
                } else {
                    return method.invoke(writeToPool, objects);
                }
            }
        });
    }
}
