package com.wsf.io;

import com.wsf.domain.Item;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 当下载的资源过大会会向磁盘读取存入文件，该接口定义
 * 向资源池读入时操作
 */
public interface IReadFromPool{
    /**
     * 从url池中读取 请求器输入文件
     * @return
     */
    ConcurrentLinkedQueue<String> readForReq();

    /**
     * 从html池中读取 解析器输入文件
     * @return
     */
    ConcurrentHashMap<String,byte[]> readForParse();

    /**
     * 从item池中读取 保存器输入文件
     * @return
     */
    ConcurrentHashMap<String, Item> readForSave();

    /**
     * 从url池中批量读取
     * @return
     */
    LinkedList<ConcurrentLinkedQueue> readForReqBatch();

    /**
     * 从html池中批量读取
     * @return
     */
    LinkedList<ConcurrentHashMap> readForParseBatch();

    /**
     * 从item池中批量读取
     * @return
     */
    LinkedList<ConcurrentHashMap> readForSaveBatch();

    /**
     * 关闭读取器，将缓存区中的数据放回数据池子
     */
    void close();
}
