package com.wsf.config;

import com.wsf.manager.impl.RequestManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 配置类，创建线程池，创建RequestBean工厂，读取config.properties文件信息，然后可以作为一个信息类供其他类调用
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
    private static Integer reqBuffer = null;
    private static Integer reqControllerThreadNumber = null;
    private static Integer reqHandlerThreadNumber = null;
    private static Integer parseControllerThreadNumber = null;
    private static Integer parseHandlerThreadNumber = null;
    private static Integer saveControllerThreadNumber = null;
    private static Integer saveHandlerThreadNumber = null;
    private static Integer IOReadBuffer = null;
    private static Integer IOWriteBuffer = null;
    private static Integer IOReadBatchSize = null;
    private static Logger logger = Logger.getLogger(Configure.class);
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
            String temp = null;
            temp = prop.getProperty("handlerThreadNumber");
            if(temp!=null){
                handlerNumber = Integer.parseInt(temp);
            }
            temp = prop.getProperty("controllerThreadNumber");
            if(temp!=null){
                controllerNumber = Integer.parseInt(temp);
            }
            temp = prop.getProperty("connTimeout");
            if(temp!=null){
                connTimeout = Integer.parseInt(temp);
            }
            temp = prop.getProperty("readTimeout");
            if(temp!=null) {
                readTimeout = Integer.parseInt(temp);
            }
            temp= prop.getProperty("reqBuffer");
            if(temp!=null){
                reqBuffer = Integer.parseInt(temp);
            }
            temp = prop.getProperty("reqControllerThreadNumber");
            if(temp!=null){
                reqControllerThreadNumber = Integer.parseInt(temp);
            }
            temp = prop.getProperty("reqHandlerThreadNumber");
            if(temp!=null){
                reqHandlerThreadNumber = Integer.parseInt(temp);
            }
            temp = prop.getProperty("parseControllerThreadNumber");
            if(temp!=null){
                parseControllerThreadNumber = Integer.parseInt(temp);
            }
            temp = prop.getProperty("parseHandlerThreadNumber");
            if(temp!=null){
                parseHandlerThreadNumber = Integer.parseInt(temp);
            }
            temp = prop.getProperty("saveControllerThreadNumber");
            if(temp!=null){
                saveControllerThreadNumber = Integer.parseInt(temp);
            }
            temp = prop.getProperty("saveHandlerThreadNumber");
            if(temp!=null){
                saveHandlerThreadNumber = Integer.parseInt(temp);
            }
            temp = prop.getProperty("io.readBuffer");
            if(temp!=null){
                IOReadBuffer = Integer.parseInt(temp);
            }
            temp = prop.getProperty("io.writeBuffer");
            if(temp!=null){
                IOWriteBuffer = Integer.parseInt(temp);
            }
            temp = prop.getProperty("io.readBatchSize");
            if(temp!=null){
                IOReadBatchSize = Integer.parseInt(temp);
            }

            protocol = prop.getProperty("protocol");
            Class.forName("com.wsf.source.Source");

        } catch (IOException e) {
            logger.error("配置文件读取出错");
            throw new ExceptionInInitializerError("配置文件读取出错");
        } catch (Exception e) {
            logger.error("初始化资源池出错");
            throw new ExceptionInInitializerError("初始化资源池出错");
        } finally {
            if(is!=null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error("关闭配置文件输入流出错");
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

    public static Integer getReqControllerThreadNumber() {
        return reqControllerThreadNumber;
    }

    public static void setReqControllerThreadNumber(Integer reqControllerThreadNumber) {
        Configure.reqControllerThreadNumber = reqControllerThreadNumber;
    }

    public static Integer getReqHandlerThreadNumber() {
        return reqHandlerThreadNumber;
    }

    public static void setReqHandlerThreadNumber(Integer reqHandlerThreadNumber) {
        Configure.reqHandlerThreadNumber = reqHandlerThreadNumber;
    }

    public static Integer getParseControllerThreadNumber() {
        return parseControllerThreadNumber;
    }

    public static void setParseControllerThreadNumber(Integer parseControllerThreadNumber) {
        Configure.parseControllerThreadNumber = parseControllerThreadNumber;
    }

    public static Integer getParseHandlerThreadNumber() {
        return parseHandlerThreadNumber;
    }

    public static void setParseHandlerThreadNumber(Integer parseHandlerThreadNumber) {
        Configure.parseHandlerThreadNumber = parseHandlerThreadNumber;
    }

    public static Integer getSaveControllerThreadNumber() {
        return saveControllerThreadNumber;
    }

    public static void setSaveControllerThreadNumber(Integer saveControllerThreadNumber) {
        Configure.saveControllerThreadNumber = saveControllerThreadNumber;
    }

    public static Integer getSaveHandlerThreadNumber() {
        return saveHandlerThreadNumber;
    }

    public static void setSaveHandlerThreadNumber(Integer saveHandlerThreadNumber) {
        Configure.saveHandlerThreadNumber = saveHandlerThreadNumber;
    }

    public static Integer getIOReadBuffer() {
        return IOReadBuffer;
    }

    public static void setIOReadBuffer(Integer IOReadBuffer) {
        Configure.IOReadBuffer = IOReadBuffer;
    }

    public static Integer getIOWriteBuffer() {
        return IOWriteBuffer;
    }

    public static void setIOWriteBuffer(Integer IOWriteBuffer) {
        Configure.IOWriteBuffer = IOWriteBuffer;
    }

    public static Integer getIOReadBatchSize() {
        return IOReadBatchSize;
    }

    public static void setIOReadBatchSize(Integer IOReadBatchSize) {
        Configure.IOReadBatchSize = IOReadBatchSize;
    }
}
