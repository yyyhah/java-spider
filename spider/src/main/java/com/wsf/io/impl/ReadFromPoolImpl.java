package com.wsf.io.impl;

import com.wsf.config.Configure;
import com.wsf.domain.Item;
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
public class ReadFromPoolImpl implements IReadFromPool {
    //url池
    private ConcurrentLinkedQueue<ConcurrentLinkedQueue> urlBuffer = Source.getUrlBuffer();
    //html池
    private ConcurrentLinkedQueue<ConcurrentHashMap> htmlBuffer = Source.getHtmlBuffer();
    //item池
    private ConcurrentLinkedQueue<ConcurrentHashMap> itemBuffer = Source.getItemBuffer();

    //读取缓存区的大小,默认值40
    private static Integer readBuffer = Configure.getIOReadBuffer() == null?40:Configure.getIOReadBuffer();

    //按批读取时的批数 req
    private static Integer reqBatchNumber = Configure.getReqControllerThreadNumber()==null?Configure.getControllerNumber()
            :Configure.getReqControllerThreadNumber();
    //按批读取时的批数 save
    private static Integer saveBatchNumber = Configure.getSaveControllerThreadNumber()==null?Configure.getControllerNumber()
            :Configure.getSaveControllerThreadNumber();
    //按批读取时的批数 parse
    private static Integer parseBatchNumber = Configure.getParseControllerThreadNumber()==null?Configure.getControllerNumber()
            :Configure.getParseControllerThreadNumber();

    //url读取缓存
    private ConcurrentLinkedQueue[] urlReadBuffer = null;
    //表示缓存数组下标
    private int urlIndex = 0;
    //html读取缓存
    private ConcurrentHashMap[] htmlReadBuffer = null;
    private int htmlIndex = 0;
    //item读取缓存
    private ConcurrentHashMap[] itemReadBuffer = null;
    private int itemIndex = 0;
    private boolean closed = false;
    private static Logger logger = Logger.getLogger(ReadFromPoolImpl.class);



    public ReadFromPoolImpl() {
        this(null);
    }
    public ReadFromPoolImpl(Integer bufferSize) {
        reqBatchNumber = reqBatchNumber==null?10:reqBatchNumber;
        parseBatchNumber = parseBatchNumber==null?10:parseBatchNumber;
        saveBatchNumber =parseBatchNumber==null?10:saveBatchNumber;
        readBuffer = bufferSize==null?readBuffer:bufferSize;
        //创建缓存数组
        urlReadBuffer = new ConcurrentLinkedQueue[readBuffer];
        htmlReadBuffer = new ConcurrentHashMap[readBuffer];
        itemReadBuffer = new ConcurrentHashMap[readBuffer];
    }

    @Override
    public ConcurrentLinkedQueue<String> readForReq() {
        //当缓存区中没有数据或者数据为null时，从池子中导入数据
        if(urlReadBuffer.length <= urlIndex || urlReadBuffer[urlIndex]==null){
            //如果池子中没有数据，直接返回null
            if(urlBuffer.size()==0){
                return null;
            }
            for(int i = 0;i < readBuffer;i++){
                if(urlBuffer.size()>0) {
                    urlReadBuffer[i] = urlBuffer.poll();
                }else{
                  urlReadBuffer[i] = null;
                }
            }
            urlIndex = 0;
        }
        return urlReadBuffer[urlIndex++];
    }

    @Override
    public ConcurrentHashMap<String, byte[]> readForParse() {
        if(htmlReadBuffer.length <= htmlIndex || htmlReadBuffer[htmlIndex]==null){
            //如果池子中没有数据，直接返回null
            if(htmlBuffer.size()==0){
                return null;
            }
            for(int i = 0;i < readBuffer;i++){
                if(htmlBuffer.size()>0) {
                    htmlReadBuffer[i] = htmlBuffer.poll();
                }else{
                    htmlReadBuffer[i] = null;
                }
            }
            htmlIndex = 0;
        }
        return htmlReadBuffer[htmlIndex++];
    }

    @Override
    public ConcurrentHashMap<String, Item> readForSave() {
        if(itemReadBuffer.length <= itemIndex || itemReadBuffer[itemIndex]==null){
            //如果池子中没有数据，直接返回null
            if(itemBuffer.size()==0){
                return null;
            }
            for(int i = 0;i < readBuffer;i++){
                if(itemBuffer.size()>0) {
                    itemReadBuffer[i] = itemBuffer.poll();
                }else{
                    itemReadBuffer[i] = null;
                }
            }
            itemIndex = 0;
        }
        return itemReadBuffer[itemIndex++];
    }

    @Override
    public LinkedList<ConcurrentLinkedQueue> readForReqBatch() {
        LinkedList<ConcurrentLinkedQueue> lists = new LinkedList<>();
        for (Integer i = 0; i < reqBatchNumber; i++) {
            ConcurrentLinkedQueue<String> strings = readForReq();
            if(strings==null){
                break;
            }
            lists.add(strings);
        }
        return lists;
    }

    @Override
    public LinkedList<ConcurrentHashMap> readForParseBatch() {
        LinkedList<ConcurrentHashMap> lists = new LinkedList<>();
        for (Integer i = 0; i < parseBatchNumber; i++) {
            ConcurrentHashMap<String, byte[]> map = readForParse();
            if(map==null){
                break;
            }
            lists.add(map);
        }
        return lists;
    }

    @Override
    public LinkedList<ConcurrentHashMap> readForSaveBatch() {
        LinkedList<ConcurrentHashMap> lists = new LinkedList<>();
        for (Integer i = 0; i < saveBatchNumber; i++) {
            ConcurrentHashMap<String, Item> map = readForSave();
            if(map==null){
                break;
            }
            lists.add(map);
        }
        return lists;
    }

    @Override
    public Boolean hasNextReq() {
        return (urlIndex < readBuffer && urlReadBuffer[urlIndex]!=null) || urlBuffer.size()>0;
    }

    @Override
    public Boolean hasNextParse() {
        return (htmlIndex < readBuffer && htmlReadBuffer[htmlIndex]!=null) || htmlBuffer.size()>0;
    }

    @Override
    public Boolean hashNextSave() {
        return (itemIndex < readBuffer && itemReadBuffer[itemIndex]!=null) || itemBuffer.size()>0;
    }

    @Override
    public void close() {
        //将全部数据保存进资源池
        if (urlIndex < readBuffer && urlReadBuffer[urlIndex] != null) {
            for (int i = urlIndex; i < readBuffer && urlReadBuffer[i]!=null; i++) {
                urlBuffer.add(urlReadBuffer[i]);
            }
        }
        if (itemIndex < readBuffer && itemReadBuffer[itemIndex] != null) {
            for (int i = itemIndex; i < readBuffer && itemReadBuffer[i] != null; i++) {
                itemBuffer.add(itemReadBuffer[i]);
            }
        }
        if (htmlIndex < readBuffer && htmlReadBuffer[htmlIndex] != null) {
            for (int i = htmlIndex; i < readBuffer && htmlReadBuffer[i] != null; i++) {
                htmlBuffer.add(htmlReadBuffer[i]);
            }
        }
        this.closed = true;
        htmlBuffer = null;
        itemBuffer = null;
        urlBuffer = null;
    }

    @Override
    public Boolean isClosed() {
        return this.closed;
    }
}
