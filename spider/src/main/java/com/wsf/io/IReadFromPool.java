package com.wsf.io;

import com.wsf.config.Configure;

import java.util.LinkedList;

/**
 * 当下载的资源过大会会向磁盘读取存入文件，该接口定义
 * 向资源池读入时操作
 */
public interface IReadFromPool<T> {
    Double minSize = Configure.getMinSize();
    Integer sourceSize = Configure.getSourceSize();

    /**
     * 从池中读取 保存器输入文件
     *
     * @return
     */
    T read();

    /**
     * 从池中批量读取
     *
     * @return
     */
    LinkedList<T> readBatch();


    /**
     * 判断池中是否还有数据
     *
     * @return true 还有数据 false 没有数据
     */
    Boolean hasNext();

    /**
     * 关闭读取器，将缓存区中的数据放回数据池子
     */
    void close();

    /**
     * 判断当前流是否关闭
     *
     * @return
     */
    Boolean isClosed();

    /**
     * 获取当前资源池大小
     * @return
     */
    Integer getCurrentSourceSize();
    /**
     * 判断池子大小，如果小于设定值，尝试从内存中读取数据
     * @return
     */
    default Boolean tooSamll(){
        return  getCurrentSourceSize()<minSize*sourceSize;
    }
}
