package com.wsf.io.impl;

import com.wsf.config.Configure;
import com.wsf.io.IWriteToPool;
import com.wsf.source.Source;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WriteToHTMLImpl implements IWriteToPool<ConcurrentHashMap<String, byte[]>> {
    //html池
    private ConcurrentLinkedQueue<ConcurrentHashMap> htmlBuffer = Source.getHtmlBuffer();
    //写入缓存区的大小,默认值40
    private static Integer writeBuffer = Configure.getIOWriteBuffer() == null ? 40 : Configure.getIOWriteBuffer();
    //html读取缓存
    private ConcurrentHashMap[] htmlWriteBuffer = null;

    private Integer htmlIndex = 0;

    private boolean closed = false;

    public WriteToHTMLImpl() {
        this(null);
    }

    public WriteToHTMLImpl(Integer bufferSize) {
        writeBuffer = bufferSize == null ? writeBuffer : bufferSize;
        htmlWriteBuffer = new ConcurrentHashMap[writeBuffer];
    }

    @Override
    public void write(ConcurrentHashMap<String, byte[]> inBuffer) {
        //如果inBuffer为null，那就不必添加了
        if (inBuffer == null || inBuffer.size() == 0) {
            return;
        }
        //如果缓存区没满，加入缓存区
        if (htmlIndex < writeBuffer) {
            htmlWriteBuffer[htmlIndex++] = inBuffer;
        } else {
            //如果缓存区满了，全部放入资源池
            flush();
            htmlWriteBuffer[htmlIndex++] = inBuffer;
        }
    }

    /**
     * 将写入html的信息全部刷入资源池
     */
    @Override
    public void flush() {
        for (int i = 0; i < htmlIndex; i++) {
            htmlBuffer.add(htmlWriteBuffer[i]);
            htmlWriteBuffer[i] = null;
        }
        htmlIndex = 0;
    }

    @Override
    public void close() {
        flush();
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
