package com.wsf.io.impl;

import com.wsf.config.Configure;
import com.wsf.domain.Item;
import com.wsf.io.IWriteToPool;
import com.wsf.source.Source;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 从资源池读取数据接口实现类,单例，必须保证线程安全
 */
@SuppressWarnings("all")
public class WriteToItemImpl implements IWriteToPool<ConcurrentHashMap<String, Item>> {
    //item池
    private ConcurrentLinkedQueue<ConcurrentHashMap> itemBuffer = Source.getItemBuffer();
    //写入缓存区的大小,默认值40
    private static Integer writeBuffer = Configure.getIOWriteBuffer() == null ? 40 : Configure.getIOWriteBuffer();
    //item读取缓存
    private ConcurrentHashMap[] itemWriteBuffer = null;

    private Integer itemIndex = 0;

    private boolean closed = false;

    public WriteToItemImpl() {
        this(null);
    }

    public WriteToItemImpl(Integer bufferSize) {
        writeBuffer = bufferSize == null ? writeBuffer : bufferSize;
        itemWriteBuffer = new ConcurrentHashMap[writeBuffer];
    }


    @Override
    public void write(ConcurrentHashMap<String, Item> inBuffer) {
        if (inBuffer == null || inBuffer.size() == 0) {
            return;
        }
        if (itemIndex < writeBuffer) {
            itemWriteBuffer[itemIndex++] = inBuffer;
        } else {
            //如果缓存区满了，全部放入资源池
            flush();
            itemWriteBuffer[itemIndex++] = inBuffer;
        }
    }


    @Override
    public void close() {
        flush();
        closed = true;
        itemBuffer = null;
    }


    /**
     * 将写入item的信息全部刷入资源池
     */
    @Override
    public void flush() {
        for (int i = 0; i < itemIndex; i++) {
            itemBuffer.add(itemWriteBuffer[i]);
            itemWriteBuffer[i] = null;
        }
        itemIndex = 0;
    }

    @Override
    public Boolean isClosed() {
        return this.closed;
    }

    @Override
    public Integer getCurrentSourceSize() {
        return itemBuffer.size();
    }
}
