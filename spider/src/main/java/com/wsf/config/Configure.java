package com.wsf.config;

import com.wsf.request.RequestFactory;
import com.wsf.request.RequestManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 配置类，创建线程池，创建RequestBean工厂，读取config.properties文件信息
 * 创建reqConBuffer 和 conReqBuffer缓存区
 */
public class Configure {
    //线程池
    private static ExecutorService executorService;
    //请求器和控制器之间的缓冲区(单向) 请求器->控制器
    private static LinkedHashMap<String,byte[]> reqConBuffer = new LinkedHashMap<String, byte[]>();
    //控制器和请求器之间的缓冲区(单向) 控制器->请求器 conReqBuffer;
    private LinkedList<String> conReqBuffer = new LinkedList<String>();
    //RequestBean工厂类
    private static RequestFactory factory;
    //保存请求头信息
    private static Map<String,String> requestHeader = new HashMap<>();

    static{
        InputStream is = RequestManager.class.getClassLoader().getResourceAsStream("config.properties");
        try {
            Properties prop = new Properties();
            prop.load(is);
            Set<String> strings = prop.stringPropertyNames();
            for (String string : strings) {
                if(string.startsWith("request.")){
                    requestHeader.put(string,prop.getProperty(string));
                }
            }
            String size = prop.getProperty("requestThreadPoolSize");
            //初始化请求器线程池大小
            executorService = Executors.newFixedThreadPool(Integer.parseInt(size));
            //初始化工厂类
            factory = new RequestFactory(reqConBuffer,requestHeader);
        } catch (IOException e) {
            //如果初始化失败，关闭线程池
            if(executorService!=null){
                executorService.shutdown();
            }
            throw new ExceptionInInitializerError("线程池初始化错误！");
        }finally {
            if(is!=null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
