package com.wsf.io.impl;

import com.wsf.config.Configure;
import com.wsf.io.IWriteToPool;
import com.wsf.source.Source;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WriteToHtml implements IWriteToPool<ConcurrentHashMap<String, byte[]>> {
    //html池
    private ConcurrentLinkedQueue<ConcurrentHashMap> htmlBuffer = Source.getHtmlBuffer();

    private boolean closed = false;


    @Override
    public void write(ConcurrentHashMap<String, byte[]> inBuffer) {
        //如果inBuffer为null，那就不必添加了
        if (inBuffer == null || inBuffer.size() == 0) {
            return;
        }
        htmlBuffer.add(inBuffer);
    }


    @Override
    public void close() {
        closed = true;
        htmlBuffer = null;
    }

    @Override
    public Boolean isClosed() {
        return this.closed;
    }

    @Override
    public Integer getCurrentSourceSize() {
        return htmlBuffer.size();
    }

}
