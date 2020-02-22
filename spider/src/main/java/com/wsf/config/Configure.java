package com.wsf.config;

import com.wsf.manager.impl.RequestManager;
import com.wsf.source.Source;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 配置类，创建线程池，创建RequestBean工厂，读取config.properties文件信息
 */
public class Configure {
    //保存请求头信息
    private static Map<String,String> requestHeader;
    //线程池的大小
    private static Integer handlerNumber = null;
    private static Integer controllerNumber = null;
    private static Integer connTimeout = null;
    private static Integer readTimeout = null;
    private static String protocol = null;
    private static Source source = null;
    private static Integer reqBuffer = null;
    static{
        InputStream is = RequestManager.class.getClassLoader().getResourceAsStream("config.properties");
        try {
            Properties prop = new Properties();
            prop.load(is);
            Set<String> strings = prop.stringPropertyNames();
            requestHeader = new HashMap<String, String>();
            for (String string : strings) {
                if(string.startsWith("request.")){
                    requestHeader.put(string,prop.getProperty(string));
                }
            }
            String poolSize1 = prop.getProperty("handlerThreadNumber");
            if(poolSize1!=null){
                handlerNumber = Integer.parseInt(poolSize1);
            }
            String poolSize2 = prop.getProperty("controllerThreadNumber");
            if(poolSize2!=null){
                controllerNumber = Integer.parseInt(poolSize2);
            }
            String conn = prop.getProperty("connTimeout");
            if(conn!=null){
                connTimeout = Integer.parseInt(conn);
            }
            String read = prop.getProperty("readTimeout");
            if(read!=null) {
                readTimeout = Integer.parseInt(read);
            }
            String reqBuffer1= prop.getProperty("reqBuffer");
            if(reqBuffer1!=null){
                reqBuffer = Integer.parseInt(reqBuffer1);
            }
            protocol = prop.getProperty("protocol");
            source = (Source)Class.forName("com.wsf.source.Source").getConstructor().newInstance();
        } catch (IOException e) {
            throw new ExceptionInInitializerError("线程池初始化错误！");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError("初始化资源池出错");
        } finally {
            if(is!=null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Integer getReqBuffer() {
        return reqBuffer;
    }

    public static void setReqBuffer(Integer reqBuffer) {
        Configure.reqBuffer = reqBuffer;
    }

    public static Map<String, String> getRequestHeader() {
        return requestHeader;
    }

    public static void setRequestHeader(Map<String, String> requestHeader) {
        Configure.requestHeader = requestHeader;
    }

    public static Integer getHandlerNumber() {
        return handlerNumber;
    }

    public static void setHandlerNumber(Integer handlerNumber) {
        Configure.handlerNumber = handlerNumber;
    }

    public static Integer getControllerNumber() {
        return controllerNumber;
    }

    public static void setControllerNumber(Integer controllerNumber) {
        Configure.controllerNumber = controllerNumber;
    }

    public static Integer getConnTimeout() {
        return connTimeout;
    }

    public static void setConnTimeout(Integer connTimeout) {
        Configure.connTimeout = connTimeout;
    }

    public static Integer getReadTimeout() {
        return readTimeout;
    }

    public static void setReadTimeout(Integer readTimeout) {
        Configure.readTimeout = readTimeout;
    }

    public static String getProtocol() {
        return protocol;
    }

    public static void setProtocol(String protocol) {
        Configure.protocol = protocol;
    }

    public static Source getSource() {
        return source;
    }

    public static void setSource(Source source) {
        Configure.source = source;
    }
}
