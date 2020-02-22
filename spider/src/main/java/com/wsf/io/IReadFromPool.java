package com.wsf.io;

import java.util.LinkedList;

/**
 * 当下载的资源过大会会向磁盘读取存入文件，该接口定义
 * 向资源池读入时操作
 */
public interface IReadFromPool{
    /**
     * 从资源池中读取 请求器输入文件
     * @return
     */
    LinkedList<String> readForRequest();

}
