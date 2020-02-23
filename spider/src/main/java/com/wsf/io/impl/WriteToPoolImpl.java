package com.wsf.io.impl;

import com.wsf.domain.Item;
import com.wsf.io.IWriteToPool;
import com.wsf.source.Source;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 从资源池读取数据接口实现类,单例，必须保证线程安全
 */
@SuppressWarnings("all")
public class WriteToPoolImpl implements IWriteToPool {
    //url池
    private static ConcurrentLinkedQueue<ConcurrentLinkedQueue> urlBuffer = Source.getUrlBuffer();
    //html池
    private static ConcurrentLinkedQueue<ConcurrentHashMap> htmlBuffer = Source.getHtmlBuffer();
    //item池
    private static ConcurrentLinkedQueue<ConcurrentHashMap> itemBuffer = Source.getItemBuffer();

    //写入缓存区的大小,默认值40
    private static Integer writeBuffer = 40;

    //url读取缓存
    private static ConcurrentLinkedQueue[] urlWriteBuffer = null;
    private static Integer urlIndex = 0;
    //html读取缓存
    private static ConcurrentHashMap[] htmlWriteBuffer = null;
    private static Integer htmlIndex = 0;
    //item读取缓存
    private static ConcurrentHashMap[] itemWriteBuffer = null;
    private static Integer itemIndex = 0;

    public WriteToPoolImpl() {
        this(null);
    }
    public WriteToPoolImpl(Integer bufferSize) {
        writeBuffer = bufferSize==null?writeBuffer:bufferSize;
        urlWriteBuffer = new ConcurrentLinkedQueue[writeBuffer];
        itemWriteBuffer = new ConcurrentHashMap[writeBuffer];
        htmlWriteBuffer = new ConcurrentHashMap[writeBuffer];
    }
    @Override
    public void writeToParse(ConcurrentHashMap<String, byte[]> inBuffer) {
        //如果inBuffer为null，那就不必添加了
        if(inBuffer==null || inBuffer.size()==0){
            return;
        }
        //如果缓存区没满，加入缓存区
        if(htmlIndex < writeBuffer){
            htmlWriteBuffer[htmlIndex++] = inBuffer;
        }else{
            //如果缓存区满了，全部放入资源池
            flushParse();
            htmlWriteBuffer[htmlIndex++] = inBuffer;
        }
    }

    @Override
    public void writeToSave(ConcurrentHashMap<String, Item> inBuffer) {
        if(inBuffer==null || inBuffer.size()==0){
            return;
        }
        if(itemIndex < writeBuffer){
            itemWriteBuffer[itemIndex++] = inBuffer;
        }else{
            //如果缓存区满了，全部放入资源池
            flushSave();
            itemWriteBuffer[itemIndex++] = inBuffer;
        }
    }

    @Override
    public void writeToReq(ConcurrentLinkedQueue<String> inBuffer) {
        if(inBuffer==null || inBuffer.size()==0){
            return;
        }
        if(urlIndex < writeBuffer){
            urlWriteBuffer[urlIndex++] = inBuffer;
        }else{
            //如果缓存区满了，全部放入资源池
            flushReq();
            urlWriteBuffer[urlIndex++] = inBuffer;
        }
    }

    @Override
    public void close() {
        flush();
    }

    /**
     * 将写入url的信息全部刷入资源池
     */
    public void flushReq(){
        for (int i = 0; i < urlIndex; i++) {
            urlBuffer.add(urlWriteBuffer[i]);
            urlWriteBuffer[i] = null;
        }
        urlIndex = 0;
    }


    /**
     * 将写入html的信息全部刷入资源池
     */
    public void flushParse(){
        for (int i = 0; i < htmlIndex; i++) {
            htmlBuffer.add(htmlWriteBuffer[i]);
            htmlWriteBuffer[i] = null;
        }
        htmlIndex = 0;
    }

    /**
     * 将写入item的信息全部刷入资源池
     */
    public void flushSave(){
        for (int i = 0; i < itemIndex; i++) {
            itemBuffer.add(itemWriteBuffer[i]);
            itemWriteBuffer[i] = null;
        }
        itemIndex = 0;
    }

    @Override
    public void flush() {
        flushSave();
        flushParse();
        flushReq();
    }
}
