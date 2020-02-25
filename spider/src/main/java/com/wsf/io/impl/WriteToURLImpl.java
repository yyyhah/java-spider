package com.wsf.io.impl;

import com.wsf.config.Configure;
import com.wsf.io.IWriteToPool;
import com.wsf.source.Source;

import java.util.concurrent.ConcurrentLinkedQueue;

public class WriteToURLImpl implements IWriteToPool<ConcurrentLinkedQueue<String>> {
    //url池
    private ConcurrentLinkedQueue<ConcurrentLinkedQueue> urlBuffer = Source.getUrlBuffer();
    //写入缓存区的大小,默认值40
    private static Integer writeBuffer = Configure.getIOWriteBuffer() == null ? 40 : Configure.getIOWriteBuffer();
    //url读取缓存
    private ConcurrentLinkedQueue[] urlWriteBuffer = null;

    private Integer urlIndex = 0;

    private boolean closed = false;

    public WriteToURLImpl() {
        this(null);
    }

    public WriteToURLImpl(Integer bufferSize) {
        writeBuffer = bufferSize == null ? writeBuffer : bufferSize;
        urlWriteBuffer = new ConcurrentLinkedQueue[writeBuffer];
    }

    @Override
    public void write(ConcurrentLinkedQueue<String> inBuffer) {
        if (inBuffer == null || inBuffer.size() == 0) {
            return;
        }
        if (urlIndex < writeBuffer) {
            urlWriteBuffer[urlIndex++] = inBuffer;
        } else {
            //如果缓存区满了，全部放入资源池
            flush();
            urlWriteBuffer[urlIndex++] = inBuffer;
        }
    }

    /**
     * 将写入url的信息全部刷入资源池
     */
    public void flush() {
        for (int i = 0; i < urlIndex; i++) {
            urlBuffer.add(urlWriteBuffer[i]);
            urlWriteBuffer[i] = null;
        }
        urlIndex = 0;
    }

    @Override
    public void close() {
        flush();
        closed = true;
        urlBuffer = null;
    }

    @Override
    public Boolean isClosed() {
        return this.closed;
    }

    @Override
    public Integer getCurrentSourceSize() {
        return urlBuffer.size();
    }
}
