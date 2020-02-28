package com.wsf.io;

import com.wsf.config.Configure;


/**
 * 当下载的资源过大会会向磁盘读取存入文件，该接口定义
 * 向资源池写入时操作
 */
public interface IWriteToPool<T> {
    Double maxSize = Configure.getMaxSize();
    Integer sourceSize = Configure.getSourceSize();
    /**
     * 写入资源池
     *
     * @param inBuffer
     */
    void write(T inBuffer);

    /**
     * 关闭读取器
     */
    void close();



    /**
     * 判断当前流是否关闭
     *
     * @return
     */
    Boolean isClosed();

    /**
     * 判断当前资源池，大小，如果大于设定值，将资源放入磁盘中
     * @return
     */
    default Boolean tooLarge(){
        return getCurrentSourceSize()>maxSize*sourceSize;
    }

    /**
     * 获取当前资源池大小
     * @return
     */
    Integer getCurrentSourceSize();

}
