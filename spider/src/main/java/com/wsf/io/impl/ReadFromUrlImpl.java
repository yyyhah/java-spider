package com.wsf.io.impl;

import com.wsf.config.Configure;
import com.wsf.io.IReadFromPool;
import com.wsf.source.Source;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

@SuppressWarnings("all")
public class ReadFromUrlImpl implements IReadFromPool<ConcurrentLinkedQueue<String>> {
    //url池
    private ConcurrentLinkedQueue<ConcurrentLinkedQueue> urlBuffer = Source.getUrlBuffer();
    //读取缓存区的大小,默认值40
    private static Integer readBuffer = Configure.getIOReadBuffer() == null ? 40 : Configure.getIOReadBuffer();
    //按批读取时的批数 req
    private static Integer reqBatchNumber = Configure.getReqControllerThreadNumber() == null ? Configure.getControllerNumber()
            : Configure.getReqControllerThreadNumber();
    //url读取缓存
    private ConcurrentLinkedQueue<String>[] urlReadBuffer = null;
    //表示缓存数组下标
    private int urlIndex = 0;

    private boolean closed = false;

    private static Logger logger = Logger.getLogger(ReadFromUrlImpl.class);

    public ReadFromUrlImpl() {
        this(null);
    }

    public ReadFromUrlImpl(Integer bufferSize) {
        reqBatchNumber = reqBatchNumber == null ? 10 : reqBatchNumber;
        readBuffer = bufferSize == null ? readBuffer : bufferSize;
        urlReadBuffer = new ConcurrentLinkedQueue[readBuffer];
    }

    @Override
    public ConcurrentLinkedQueue<String> read() {
        //当缓存区中没有数据或者数据为null时，从池子中导入数据
        if (urlReadBuffer.length <= urlIndex || urlReadBuffer[urlIndex] == null) {
            //如果池子中没有数据，直接返回null
            if (urlBuffer.size() == 0) {
                return null;
            }
            for (int i = 0; i < readBuffer; i++) {
                if (urlBuffer.size() > 0) {
                    urlReadBuffer[i] = urlBuffer.poll();
                } else {
                    urlReadBuffer[i] = null;
                }
            }
            urlIndex = 0;
        }
        return urlReadBuffer[urlIndex++];
    }

    @Override
    public LinkedList<ConcurrentLinkedQueue<String>> readBatch() {
        LinkedList<ConcurrentLinkedQueue<String>> lists = new LinkedList<>();
        for (Integer i = 0; i < reqBatchNumber; i++) {
            ConcurrentLinkedQueue<String> strings = read();
            if (strings == null) {
                break;
            }
            lists.add(strings);
        }
        return lists;
    }

    @Override
    public Boolean hasNext() {
        return (urlIndex < readBuffer && urlReadBuffer[urlIndex] != null) || urlBuffer.size() > 0 || Source.getHasUrlResource();
    }

    @Override
    public void close() {
        if (urlIndex < readBuffer && urlReadBuffer[urlIndex] != null) {
            for (int i = urlIndex; i < readBuffer && urlReadBuffer[i] != null; i++) {
                urlBuffer.add(urlReadBuffer[i]);
            }
        }
        this.closed = true;
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
