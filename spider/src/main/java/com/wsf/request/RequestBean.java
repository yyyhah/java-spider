package com.wsf.request;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * @author wsf
 * 该类为请求器的最小单元，用于请求一个url连接，多例
 */
public class RequestBean implements Runnable{

    //需要访问的url地址
    private String url;
    //缓存区大小
    private int size = 1024;
    //请求器和调度器之间的缓存区(单向)，这其实是一个linkedHashMap
    private Map<String,byte[]> outBuffer;
    //请求头信息
    private Map<String, String> requestHeader;
    //设置超时时间,设置为静态变量，那所有的类都是这个时间
    private static Integer connectTimeOut = null;
    //设置资源传输超时时间
    private static Integer readTimeout = null;
    /**
     * 构造方法，传入需要访问的网址以及请求头信息
     * @param url
     */
    public RequestBean(String url,Map outBuffer,Map<String, String> requestHeader) {
        //如果url是以http开头的，那就认为url中包含了协议
        this.url = url;
        this.outBuffer = outBuffer;
        this.requestHeader = requestHeader;
    }

    /**
     * 线程执行方法，会请求网址，将网址返回的静态资源保存到请求器和调度器之间的缓存中
     */
    public void run() {
        System.out.println("RequestBean 当前线程为:"+Thread.currentThread());
        BufferedInputStream bis = null;
        try {
            //请求资源
            URL url = new URL(this.url);
            URLConnection connection = url.openConnection();
            //为请求配置请求头信息
            if(requestHeader!=null && requestHeader.size()>0){
                for (Map.Entry<String, String> entry : requestHeader.entrySet()) {
                    connection.setRequestProperty(entry.getKey().split("\\.")[1],entry.getValue());
                }
            }
            /**
             * 设置超时时间
             */
            if(connectTimeOut!=null){
                connection.setConnectTimeout(connectTimeOut);
            }
            if(readTimeout!=null){
                connection.setReadTimeout(connectTimeOut);
            }
            //建立实际连接
            connection.connect();
            //将资源写入ByteArrayOutputStream中
            bis = new BufferedInputStream(connection.getInputStream());
            byte[] bytes = new byte[this.size];
            int len = 0;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while((len=bis.read(bytes))!=-1){
                bos.write(bytes,0,len);
            }
            System.out.println("Bean当前线程"+Thread.currentThread());
            //将资源保存到请求器和调度器之间的缓存区中
            outBuffer.put(this.url,bos.toByteArray());
        } catch (IOException e) {
            //如果网址无法访问也会封装到缓存区，交给调度器处理
            outBuffer.put(this.url,null);
            throw new RuntimeException("无法访问到 "+this.url);
        }finally {
            if(bis!=null){
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Integer getConnTimeOut() {
        return connectTimeOut;
    }

    public static void setConnTimeOut(Integer connectTimeOut) {
        RequestBean.connectTimeOut = connectTimeOut;
    }

    public static Integer getreadTimeout() {
        return readTimeout;
    }

    public static void setReadTimeout(Integer readTimeout) {
        RequestBean.readTimeout = readTimeout;
    }
}
