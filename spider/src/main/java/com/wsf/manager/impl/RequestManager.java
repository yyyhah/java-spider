package com.wsf.manager.impl;


import com.wsf.controller.request.impl.RequestControllerImpl;
import com.wsf.factory.RequestFactory;
import com.wsf.manager.IHandlerManager;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 该类用于管理请求器的请求，多例
 */
public class RequestManager implements IHandlerManager<ConcurrentLinkedQueue<String>,ConcurrentHashMap<String,byte[]>> {
    //线程池
    private ExecutorService executorService;
    //请求器和控制器之间的缓冲区(单向) 请求器->控制器
    private ConcurrentHashMap<String,byte[]> outBuffer;
    //控制器和请求器之间的缓冲区(单向) 控制器->请求器 conReqBuffer;
    private ConcurrentLinkedQueue<String> inBuffer;
    //RequestBean工厂类
    private RequestFactory factory;
    //保存请求头信息
    private Map<String,String> requestHeader;
    //连接超时时间
    private Integer connTimeout = null;
    //读取超时时间
    private Integer readTimeout = null;
    //线程池大小，默认为10
    private Integer size = 10;
    //管理器的id
    private Integer manaferId = null;

    public RequestManager() {
    }

    public RequestManager(Integer id,Map<String, String> requestHeader, Integer connTimeout, Integer readTimeout, Integer size) {
        this.manaferId = id;
        this.requestHeader = requestHeader;
        this.connTimeout = connTimeout;
        this.readTimeout = readTimeout;
        this.size = size==null?this.size:size;
    }

    /**
     * 初始化之前必须先设置数据源 inBuffer 和 outBuffer
     * 创建Request工厂，和线程池
     */
    @Override
    public void init(){
        //创建线程池
        executorService = Executors.newFixedThreadPool(size);
        //设置请求头，创建request工厂
        factory = new RequestFactory(this.outBuffer,this.connTimeout,this.readTimeout,this.requestHeader);
        System.out.println("RequestManager:当前线程池线程数目："+size);
    }

    /**
     * 请求器管理器关闭方法
     */
    @Override
    public void destroy(){
        //关闭线程池
        executorService.shutdown();
        try {
            //5秒后检测线程是否关闭，如果没有关闭，强制关闭
            if(!executorService.awaitTermination(5000, TimeUnit.SECONDS)){
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            e.printStackTrace();
        }
    }

    @Override
    public ExecutorService getExecutorService() {
        return executorService;
    }

    @Override
    public Integer getConnTimeout() {
        return this.connTimeout;
    }

    @Override
    public void setConnTimeout(Integer time) {
        this.connTimeout = time;
        //为工厂设置超时
        factory.setConnTimeout(time);
    }

    @Override
    public Integer getReadTimeout() {
        return this.readTimeout;
    }

    @Override
    public void setReadTimeout(Integer time) {
        this.readTimeout = time;
        factory.setReadTimeout(time);
    }

    @Override
    public Integer getSize() {
        return size;
    }

    @Override
    public void setSize(Integer size) {
        this.size = size;
    }

    public ConcurrentHashMap<String, byte[]> getOutBuffer() {
        return outBuffer;
    }

    public void setOutBuffer(ConcurrentHashMap<String, byte[]> outBuffer) {
        this.outBuffer = outBuffer;
        factory.setOutBuffer(outBuffer);
    }

    public ConcurrentLinkedQueue<String> getInBuffer() {
        return inBuffer;
    }

    public void setInBuffer(ConcurrentLinkedQueue<String> inBuffer) {
        this.inBuffer = inBuffer;
    }

    @Override
    public void run() {
        if(outBuffer==null){
            throw new RuntimeException("RequestManager的outBuffer为空,请设置");
        }
        if(inBuffer==null||inBuffer.size()==0){
            System.out.println("RequestManager的inBuffer为空");
            return;
        }
        LinkedList<Future> lists = new LinkedList<Future>();
        System.out.println("当前线程:"+Thread.currentThread());
        System.out.println("RequestManager中的线程池:"+executorService);
        while(inBuffer.size()>0){
            //从缓存区中获取一个url链接,并弹出
            String url = inBuffer.poll();
            //开启请求线程
            Future future = executorService.submit(factory.getRequestBean(url));
            lists.add(future);
        }
        //阻塞线程，等待全部子线程执行完毕
        for (Future list : lists) {
            try {
                list.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        RequestControllerImpl.finish(outBuffer,manaferId);
    }
}
