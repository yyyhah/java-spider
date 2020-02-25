package com.wsf.io.impl;

import com.wsf.config.Configure;
import com.wsf.io.IReadFromPool;
import com.wsf.source.Source;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@SuppressWarnings("all")
public class ReadFromHTMLImpl implements IReadFromPool<ConcurrentHashMap> {
    //html池
    private ConcurrentLinkedQueue<ConcurrentHashMap> htmlBuffer = Source.getHtmlBuffer();
    //读取缓存区的大小,默认值40
    private static Integer readBuffer = Configure.getIOReadBuffer() == null ? 40 : Configure.getIOReadBuffer();
    //按批读取时的批数 parse
    private static Integer parseBatchNumber = Configure.getParseControllerThreadNumber() == null ? Configure.getControllerNumber()
            : Configure.getParseControllerThreadNumber();
    //html读取缓存
    private ConcurrentHashMap[] htmlReadBuffer = null;

    private int htmlIndex = 0;

    private boolean closed = false;

    private static Logger logger = Logger.getLogger(ReadFromHTMLImpl.class);

    public ReadFromHTMLImpl() {
        this(null);
    }

    public ReadFromHTMLImpl(Integer bufferSize) {
        parseBatchNumber = parseBatchNumber == null ? 10 : parseBatchNumber;
        readBuffer = bufferSize == null ? readBuffer : bufferSize;
        htmlReadBuffer = new ConcurrentHashMap[readBuffer];
    }

    @Override
    public ConcurrentHashMap<String, byte[]> read() {
        if (htmlReadBuffer.length <= htmlIndex || htmlReadBuffer[htmlIndex] == null) {
            //如果池子中没有数据，直接返回null
            if (htmlBuffer.size() == 0) {
                return null;
            }
            for (int i = 0; i < readBuffer; i++) {
                if (htmlBuffer.size() > 0) {
                    htmlReadBuffer[i] = htmlBuffer.poll();
                } else {
                    htmlReadBuffer[i] = null;
                }
            }
            htmlIndex = 0;
        }
        return htmlReadBuffer[htmlIndex++];
    }

    @Override
    public LinkedList<ConcurrentHashMap> readBatch() {
        LinkedList<ConcurrentHashMap> lists = new LinkedList<>();
        for (Integer i = 0; i < parseBatchNumber; i++) {
            ConcurrentHashMap<String, byte[]> map = read();
            if (map == null) {
                break;
            }
            lists.add(map);
        }
        return lists;
    }

    @Override
    public Boolean hasNext() {
        return (htmlIndex < readBuffer && htmlReadBuffer[htmlIndex] != null) || htmlBuffer.size() > 0 || Source.getHasHtmlResource();
    }

    @Override
    public void close() {
        if (htmlIndex < readBuffer && htmlReadBuffer[htmlIndex] != null) {
            for (int i = htmlIndex; i < readBuffer && htmlReadBuffer[i] != null; i++) {
                htmlBuffer.add(htmlReadBuffer[i]);
            }
        }
        htmlBuffer = null;
        this.closed = true;
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
