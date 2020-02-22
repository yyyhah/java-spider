package com.wsf.io.impl;

import com.wsf.io.IReadFromPool;
import com.wsf.source.Source;

import java.util.LinkedList;

/**
 * 从资源池读取数据接口实现类,单例,必须保证线程安全
 */
public class ReadFromReqPoolImpl implements IReadFromPool {
    private static Source source;
    private static LinkedList<String> reqInBuffer;
    //reqInBuffer 和 ReqOutBuffer的大小
    private static Integer reqBuffer = 10;

    public ReadFromReqPoolImpl(Source source,Integer size) {
        source = source;
        reqInBuffer = source.getReqInBuffer();
        reqBuffer = size;
    }

    /**
     * 如果有值，返回，没有返回null
     * @return
     */
    @Override
    public LinkedList<String> readForRequest() {
        LinkedList<String> rsValue = new LinkedList<String>();
        synchronized (reqInBuffer){
            for(int i = 0;i<reqBuffer&&reqInBuffer.size()>0;i++){
                rsValue.add(reqInBuffer.pop());
            }
        }
        return  rsValue;
    }

}
