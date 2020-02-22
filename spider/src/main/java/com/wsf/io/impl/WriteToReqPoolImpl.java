package com.wsf.io.impl;

import com.wsf.io.IWriteToPool;
import com.wsf.source.Source;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 从资源池读取数据接口实现类,单例，必须保证线程安全
 */
public class WriteToReqPoolImpl implements IWriteToPool {
    private static Source source;
    private static LinkedHashMap<String,byte[]> reqOutBuffer;

    public WriteToReqPoolImpl(Source source) {
        this.source = source;
        this.reqOutBuffer = source.getReqOutBuffer();
    }
    @Override
    public void writeFromRequest(LinkedHashMap<String, byte[]> inBuffer) {
        synchronized (reqOutBuffer){
            reqOutBuffer.putAll(inBuffer);
        }
    }


}
