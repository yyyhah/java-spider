package com.wsf.factory.io;

import com.wsf.io.IReadFromPool;
import com.wsf.io.IWriteToPool;
import com.wsf.io.impl.*;
import com.wsf.source.Source;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@SuppressWarnings("all")
public class IOFactory {
    /**
     * 获取一个读取器 读
     *
     * @param bufferSize
     * @return
     */

    public static IReadFromPool getReqReadConnect(Integer bufferSize,Boolean diskSave) {
        ReadFromUrlImpl readFromPool = new ReadFromUrlImpl(bufferSize);
        return (IReadFromPool) Proxy.newProxyInstance(ReadFromUrlImpl.class.getClassLoader(), ReadFromUrlImpl.class.getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                String name = method.getName();
                //判断当前流是否已经关闭
                if (readFromPool.isClosed()) {
                    if (name.equals("close") || name.equals("isClosed")) {
                        method.invoke(readFromPool, objects);
                    } else {
                        System.out.println("当前流已经关闭!");
                    }
                    return null;
                } else {
                    //在执行读操作之前，判断内存中的数据是否过小，如果过于小，尝试从磁盘中读取数据到内存
                    if(diskSave&&name.startsWith("read")&&readFromPool.tooSamll()){
                        Source.loadUrl();
                    }
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
    public static IWriteToPool getReqWriteConnect(Boolean diskSave) {
        WriteToHTMLImpl writeToPool = new WriteToHTMLImpl();
        return (IWriteToPool) Proxy.newProxyInstance(WriteToHTMLImpl.class.getClassLoader(), WriteToHTMLImpl.class.getInterfaces(), new InvocationHandler() {
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
                    if(diskSave && name.startsWith("write")&&writeToPool.tooLarge()){
                        Source.unloadHtml();
                    }
                    return method.invoke(writeToPool, objects);
                }
            }
        });
    }


    public static IReadFromPool getParseReadConnect(Integer bufferSize,Boolean diskSave) {
        ReadFromHTMLImpl readFromPool = new ReadFromHTMLImpl(bufferSize);
        return (IReadFromPool) Proxy.newProxyInstance(ReadFromHTMLImpl.class.getClassLoader(), ReadFromHTMLImpl.class.getInterfaces(), new InvocationHandler() {
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
                    if(diskSave && name.startsWith("read")&&readFromPool.tooSamll()){
                        Source.loadHtml();
                    }
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
    public static IWriteToPool getParseWriteConnect(Boolean diskSave) {
        WriteToItemImpl writeToPool = new WriteToItemImpl();
        return (IWriteToPool) Proxy.newProxyInstance(WriteToItemImpl.class.getClassLoader(), WriteToItemImpl.class.getInterfaces(), new InvocationHandler() {
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
                    if(diskSave && name.startsWith("write")&&writeToPool.tooLarge()){
                        Source.unloadItem();
                    }
                    return method.invoke(writeToPool, objects);
                }
            }
        });
    }

    public static IReadFromPool getSaveReadConnect(Integer bufferSize,Boolean diskSave) {
        ReadFromItemImpl readFromPool = new ReadFromItemImpl(bufferSize);
        return (IReadFromPool) Proxy.newProxyInstance(ReadFromItemImpl.class.getClassLoader(), ReadFromItemImpl.class.getInterfaces(), new InvocationHandler() {
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
                    if(diskSave && name.startsWith("read")&&readFromPool.tooSamll()){
                        Source.loadItem();
                    }
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
    public static IWriteToPool getSaveWriteConnect(Boolean diskSave) {
        WriteToURLImpl writeToPool = new WriteToURLImpl();
        return (IWriteToPool) Proxy.newProxyInstance(WriteToURLImpl.class.getClassLoader(), WriteToURLImpl.class.getInterfaces(), new InvocationHandler() {
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
                    if(diskSave && name.startsWith("write")&&writeToPool.tooLarge()){
                        Source.unloadUrl();
                    }
                    return method.invoke(writeToPool, objects);
                }
            }
        });
    }

}
