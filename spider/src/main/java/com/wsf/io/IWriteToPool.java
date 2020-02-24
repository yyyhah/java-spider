package com.wsf.io;

import com.wsf.domain.Item;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 当下载的资源过大会会向磁盘读取存入文件，该接口定义
 * 向资源池写入时操作
 */
public interface IWriteToPool {
    /**
     * 写入html池
     *
     * @param inBuffer
     */
    void writeToParse(ConcurrentHashMap<String, byte[]> inBuffer);

    /**
     * 写入item池
     *
     * @param inBuffer
     */
    void writeToSave(ConcurrentHashMap<String, Item> inBuffer);

    /**
     * 写入url池
     *
     * @param inBuffer
     */
    void writeToReq(ConcurrentLinkedQueue<String> inBuffer);

    /**
     * 关闭读取器
     */
    void close();

    /**
     * 刷新读取器
     */
    void flush();


    /**
     * 判断当前流是否关闭
     *
     * @return
     */
    Boolean isClosed();
}
