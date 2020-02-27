package com.wsf.config;

import com.wsf.request.manager.impl.RequestManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 配置类，创建线程池，创建RequestBean工厂，读取config.properties文件信息，然后可以作为一个信息类供其他类调用
 */
public class Configure {
    //保存请求头信息
    private static Map<String, String> requestHeader;
    //线程池的大小
    private static  Integer handlerNumber;
    private static  Integer controllerNumber;
    private static  Integer connTimeout;
    private static  Integer readTimeout;
    private static  String protocol;
    private static  Integer reqBuffer;
    private static  Integer reqControllerThreadNumber;
    private static  Integer reqHandlerThreadNumber;
    private static  Integer parseControllerThreadNumber;
    private static  Integer parseHandlerThreadNumber;
    private static  Integer saveControllerThreadNumber;
    private static  Integer saveHandlerThreadNumber;
    private static  Integer IOReadBuffer;
    private static  Integer IOWriteBuffer;
    private static  Integer IOReadBatchSize;
    private static  String encodeType;
    private static  Integer sourceSize;
    private static  Double maxSize;
    private static  Double minSize;
    private static  String sourceFile;
    private static  Boolean diskSave;
    private static Logger logger = Logger.getLogger(Configure.class);

    static {
        InputStream is = RequestManager.class.getClassLoader().getResourceAsStream("config.properties");
        try {
            Properties prop = new Properties();
            prop.load(is);
            Set<String> strings = prop.stringPropertyNames();
            requestHeader = new HashMap<String, String>();
            for (String string : strings) {
                if (string.startsWith("request.")) {
                    requestHeader.put(string, prop.getProperty(string));
                }
            }

            String temp = null;
            temp = prop.getProperty("handlerThreadNumber");
            if (temp != null) {
                handlerNumber = Integer.parseInt(temp);
            }
            temp = prop.getProperty("controllerThreadNumber");
            if (temp != null) {
                controllerNumber = Integer.parseInt(temp);
            }
            temp = prop.getProperty("connTimeout");
            if (temp != null) {
                connTimeout = Integer.parseInt(temp);
            }
            temp = prop.getProperty("readTimeout");
            if (temp != null) {
                readTimeout = Integer.parseInt(temp);
            }
            temp = prop.getProperty("reqBuffer");
            if (temp != null) {
                reqBuffer = Integer.parseInt(temp);
            }
            temp = prop.getProperty("reqControllerThreadNumber");
            if (temp != null) {
                reqControllerThreadNumber = Integer.parseInt(temp);
            }
            temp = prop.getProperty("reqHandlerThreadNumber");
            if (temp != null) {
                reqHandlerThreadNumber = Integer.parseInt(temp);
            }
            temp = prop.getProperty("parseControllerThreadNumber");
            if (temp != null) {
                parseControllerThreadNumber = Integer.parseInt(temp);
            }
            temp = prop.getProperty("parseHandlerThreadNumber");
            if (temp != null) {
                parseHandlerThreadNumber = Integer.parseInt(temp);
            }
            temp = prop.getProperty("saveControllerThreadNumber");
            if (temp != null) {
                saveControllerThreadNumber = Integer.parseInt(temp);
            }
            temp = prop.getProperty("saveHandlerThreadNumber");
            if (temp != null) {
                saveHandlerThreadNumber = Integer.parseInt(temp);
            }
            temp = prop.getProperty("io.readBuffer");
            if (temp != null) {
                IOReadBuffer = Integer.parseInt(temp);
            }
            temp = prop.getProperty("io.writeBuffer");
            if (temp != null) {
                IOWriteBuffer = Integer.parseInt(temp);
            }
            temp = prop.getProperty("io.readBatchSize");
            if (temp != null) {
                IOReadBatchSize = Integer.parseInt(temp);
            }
            temp = prop.getProperty("sourceSize");
            if(temp!=null){
                sourceSize = Integer.parseInt(temp);
            }
            temp = prop.getProperty("maxSize");
            if(temp!=null){
                maxSize = Double.parseDouble(temp);
            }
            temp = prop.getProperty("minSize");
            if(temp!=null){
                minSize = Double.parseDouble(temp);
            }
            temp = prop.getProperty("diskSave");
            if(temp!=null){
                diskSave = Boolean.parseBoolean(temp);
            }
            temp = prop.getProperty("gzip");
            sourceFile = prop.getProperty("sourceFile");
            encodeType = prop.getProperty("encodeType");
            protocol = prop.getProperty("protocol");
            Class.forName("com.wsf.source.Source");

        } catch (IOException e) {
            logger.error("配置文件读取出错");
            throw new ExceptionInInitializerError("配置文件读取出错");
        } catch (Exception e) {
            logger.error("初始化资源池出错");
            throw new ExceptionInInitializerError("初始化资源池出错");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error("关闭配置文件输入流出错");
                }
            }
        }
    }

    public static Integer getSourceSize() {
        return sourceSize;
    }

    public static Double getMaxSize() {
        return maxSize;
    }


    public static Double getMinSize() {
        return minSize;
    }


    public static Integer getReqBuffer() {
        return reqBuffer;
    }


    public static Map<String, String> getRequestHeader() {
        return requestHeader;
    }


    public static Integer getHandlerNumber() {
        return handlerNumber;
    }


    public static Integer getControllerNumber() {
        return controllerNumber;
    }


    public static Integer getConnTimeout() {
        return connTimeout;
    }


    public static Integer getReadTimeout() {
        return readTimeout;
    }




    public static Integer getReqControllerThreadNumber() {
        return reqControllerThreadNumber;
    }




    public static Integer getParseControllerThreadNumber() {
        return parseControllerThreadNumber;
    }


    public static Integer getParseHandlerThreadNumber() {
        return parseHandlerThreadNumber;
    }


    public static Integer getSaveControllerThreadNumber() {
        return saveControllerThreadNumber;
    }


    public static Integer getSaveHandlerThreadNumber() {
        return saveHandlerThreadNumber;
    }


    public static Integer getIOReadBuffer() {
        return IOReadBuffer;
    }


    public static Integer getIOWriteBuffer() {
        return IOWriteBuffer;
    }


    public static Integer getIOReadBatchSize() {
        return IOReadBatchSize;
    }


    public static String getEncodeType() {
        return encodeType;
    }


    public static String getSourceFile() {
        return sourceFile;
    }


    public static Boolean getDiskSave() {
        return diskSave;
    }


    public static String getProtocol() {
        return protocol;
    }

    public static Integer getReqHandlerThreadNumber() {
        return reqHandlerThreadNumber;
    }
}
