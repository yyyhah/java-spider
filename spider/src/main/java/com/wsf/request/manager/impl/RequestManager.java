package com.wsf.request.manager.impl;


import com.wsf.controller.request.impl.RequestControllerImpl;
import com.wsf.factory.request.RequestFactory;
import com.wsf.request.manager.IHandlerManager;
import com.wsf.request.bean.RequestBean;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 该类用于管理请求器的请求，多例
 */
public class RequestManager implements IHandlerManager<ConcurrentLinkedQueue<String>, ConcurrentHashMap<String, byte[]>> {
    //线程池
    private ExecutorService executorService;
    //控制器和请求器之间的缓冲区(单向) 控制器->请求器 conReqBuffer;
    private ConcurrentLinkedQueue<String> inBuffer;
    //RequestBean工厂类
    private RequestFactory factory;
    //保存请求头信息
    private Map<String, String> requestHeader;
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

    public RequestManager(Integer id, Map<String, String> requestHeader, Integer connTimeout, Integer readTimeout, Integer size) {
        this.manaferId = id;
        this.requestHeader = requestHeader;
        this.connTimeout = connTimeout;
        this.readTimeout = readTimeout;
        this.size = size == null ? this.size : size;
    }

    /**
     * 初始化之前必须先设置数据源 inBuffer 和 outBuffer
     * 创建Request工厂，和线程池
     */
    @Override
    public void init() {
        //创建线程池
        executorService = Executors.newFixedThreadPool(size);
        //设置请求头，创建request工厂
        factory = new RequestFactory(this.connTimeout, this.readTimeout, this.requestHeader);
    }

    /**
     * 请求器管理器关闭方法
     */
    @Override
    public void destroy() {
        //关闭线程池
        System.out.println("Manager:" + executorService);
        executorService.shutdown();
        try {
            //5秒后检测线程是否关闭，如果没有关闭，强制关闭
            if (!executorService.awaitTermination(5000, TimeUnit.SECONDS)) {
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

    public ConcurrentLinkedQueue<String> getInBuffer() {
        return inBuffer;
    }

    public void setInBuffer(ConcurrentLinkedQueue<String> inBuffer) {
        this.inBuffer = inBuffer;
    }

    @Override
    public void run() {
        if (inBuffer == null || inBuffer.size() == 0) {
            System.out.println("RequestManager的inBuffer为空");
            return;
        }
        Map<Future, String> lists = new HashMap<>();
        while (inBuffer.size() > 0) {
            //从缓存区中获取一个url链接,并弹出
            String url = inBuffer.poll();
            //开启请求线程
            RequestBean requestBean = factory.getRequestBean(url);
            Future<Object[]> future = executorService.submit(requestBean);
            lists.put(future, url);
        }
        System.out.println("RequestManager等待关闭前:" + executorService);
        ConcurrentHashMap<String, byte[]> outBuffer = new ConcurrentHashMap<>();
        //阻塞线程，等待全部子线程执行完毕
        Set<Map.Entry<Future, String>> entries = lists.entrySet();
        for (Map.Entry<Future, String> entry : entries) {
            try {
                Object[] ret = (Object[]) entry.getKey().get();
                outBuffer.put((String) ret[0], (byte[]) ret[1]);
            } catch (Exception e) {
                Logger logger = Logger.getLogger(getClass());
                logger.error("requestManager,id为" + manaferId + "执行线程" + Thread.currentThread() + "出错");
                outBuffer.put(entry.getValue(), new byte[]{0});
            }
        }
        System.out.println("RequestManager等待关闭后:" + executorService);
        RequestControllerImpl.finish(outBuffer, manaferId);
    }
}
