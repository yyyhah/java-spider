package com.wsf.io.impl;

import com.wsf.config.Configure;
import com.wsf.domain.Item;
import com.wsf.domain.impl.BaseItem;
import com.wsf.io.IWriteToPool;
import com.wsf.source.Source;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 从资源池读取数据接口实现类,单例，必须保证线程安全
 */
@SuppressWarnings("all")
public class WriteToItem implements IWriteToPool<ConcurrentHashMap<String, Item>> {
    //item池
    private ConcurrentLinkedQueue<ConcurrentHashMap> itemBuffer = Source.getItemBuffer();

    private boolean closed = false;



    @Override
    public void write(ConcurrentHashMap<String, Item> inBuffer) {
        if (inBuffer == null || inBuffer.size() == 0) {
            return;
        }
        itemBuffer.add(inBuffer);
    }


    @Override
    public void close() {
        closed = true;
        itemBuffer = null;
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
