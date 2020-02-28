package com.wsf.io.impl;

import com.wsf.config.Configure;
import com.wsf.io.IWriteToPool;
import com.wsf.source.Source;

import java.util.concurrent.ConcurrentLinkedQueue;

public class WriteToUrl implements IWriteToPool<ConcurrentLinkedQueue<String>> {
    //url池
    private ConcurrentLinkedQueue<ConcurrentLinkedQueue> urlBuffer = Source.getUrlBuffer();

    private boolean closed = false;
    @Override
    public void write(ConcurrentLinkedQueue<String> inBuffer) {
        if (inBuffer == null || inBuffer.size() == 0) {
            return;
        }
        urlBuffer.add(inBuffer);
    }

    @Override
    public void close() {
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
