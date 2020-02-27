package com.wsf.io.impl;

import com.wsf.config.Configure;
import com.wsf.domain.impl.BaseItem;
import com.wsf.io.IReadFromPool;
import com.wsf.source.Source;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 从资源池读取数据接口实现类
 */

@SuppressWarnings("all")
public class ReadFromItem implements IReadFromPool<ConcurrentHashMap> {
    //item池
    private ConcurrentLinkedQueue<ConcurrentHashMap> itemBuffer = Source.getItemBuffer();

    //读取缓存区的大小,默认值40
    private static Integer readBuffer = Configure.getIOReadBuffer() == null ? 40 : Configure.getIOReadBuffer();

    //按批读取时的批数 save
    private static Integer saveBatchNumber = Configure.getSaveControllerThreadNumber() == null ? Configure.getControllerNumber()
            : Configure.getSaveControllerThreadNumber();

    //item读取缓存
    private ConcurrentHashMap[] itemReadBuffer = null;

    private int itemIndex = 0;

    private boolean closed = false;

    private static Logger logger = Logger.getLogger(ReadFromItem.class);


    public ReadFromItem() {
        this(null);
    }

    public ReadFromItem(Integer bufferSize) {

        saveBatchNumber = saveBatchNumber == null ? 10 : saveBatchNumber;
        readBuffer = bufferSize == null ? readBuffer : bufferSize;
        //创建缓存数组


        itemReadBuffer = new ConcurrentHashMap[readBuffer];
    }


    @Override
    public ConcurrentHashMap<String, BaseItem> read() {
        if (itemReadBuffer.length <= itemIndex || itemReadBuffer[itemIndex] == null) {
            //如果池子中没有数据，直接返回null
            if (itemBuffer.size() == 0) {
                return null;
            }
            for (int i = 0; i < readBuffer; i++) {
                if (itemBuffer.size() > 0) {
                    itemReadBuffer[i] = itemBuffer.poll();
                } else {
                    itemReadBuffer[i] = null;
                }
            }
            itemIndex = 0;
        }
        return itemReadBuffer[itemIndex++];
    }


    @Override
    public LinkedList<ConcurrentHashMap> readBatch() {
        LinkedList<ConcurrentHashMap> lists = new LinkedList<>();
        for (Integer i = 0; i < saveBatchNumber; i++) {
            ConcurrentHashMap<String, BaseItem> map = read();
            if (map == null) {
                break;
            }
            lists.add(map);
        }
        return lists;
    }


    @Override
    public Boolean hasNext() {
        return (itemIndex < readBuffer && itemReadBuffer[itemIndex] != null) || itemBuffer.size() > 0 || Source.getHasItemResource();
    }

    @Override
    public void close() {
        //将全部数据保存进资源池

        if (itemIndex < readBuffer && itemReadBuffer[itemIndex] != null) {
            for (int i = itemIndex; i < readBuffer && itemReadBuffer[i] != null; i++) {
                itemBuffer.add(itemReadBuffer[i]);
            }
        }

        this.closed = true;

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
