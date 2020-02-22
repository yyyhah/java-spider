package com.wsf.io;

import java.util.List;
import java.util.LinkedHashMap;
/**
 * 当下载的资源过大会会向磁盘读取存入文件，该接口定义
 * 向资源池写入时操作
 */
public interface IWriteToPool {
    /**
     * 写入从请求器中获取的信息
     * @param inBuffer
     */
    void writeFromRequest(LinkedHashMap<String,byte[]> inBuffer);

}
