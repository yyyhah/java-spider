package com.wsf.request;


import java.util.*;
import java.util.concurrent.ExecutorService;


public class RequestManager {
    //线程池
    private static ExecutorService executorService;
    //请求器和控制器之间的缓冲区(单向) 请求器->控制器
    private static LinkedHashMap<String,byte[]> reqConBuffer;
    //控制器和请求器之间的缓冲区(单向) 控制器->请求器 conReqBuffer;
    private LinkedList<String> conReqBuffer;
    //RequestBean工厂类
    private static RequestFactory factory;
    //保存请求头信息
    private static Map<String,String> requestHeader;


    public RequestManager(LinkedList<String> conReqBuffer) {
        this.conReqBuffer = conReqBuffer;
    }

    public void start(){
        //当控制器与请求器之间的缓存区有消息时
        while(conReqBuffer.size()>0){
            //从缓存区中获取一个url链接
            String url = conReqBuffer.getFirst();
            //开启请求线程
            executorService.submit(factory.getRequestBean(url));
            conReqBuffer.remove(url);
        }
    }
    public void close(){
        //关闭线程池
        executorService.shutdown();
    }
}
