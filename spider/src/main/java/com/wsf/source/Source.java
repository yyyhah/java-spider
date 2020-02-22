package com.wsf.source;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * 资源池
 */
public class Source {
    private static LinkedList<String> reqInBuffer;
    private static LinkedHashMap<String,byte[]> reqOutBuffer;

    //初始化资源池
    static {
        reqInBuffer =new LinkedList<>();
        reqOutBuffer = new LinkedHashMap<>();
    }

    public static LinkedList<String> getReqInBuffer() {
        return reqInBuffer;
    }

    //一个时刻只能一个线程在存放数据
    public static void setReqInBuffer(LinkedList<String> buffer) {
        reqInBuffer = buffer;
    }


    public static LinkedHashMap<String,byte[]> getReqOutBuffer() {
        return reqOutBuffer;
    }

    public static void setReqOutBuffer(LinkedHashMap<String,byte[]> buffer) {
        reqOutBuffer = buffer;
    }
}
