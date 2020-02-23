package com.wsf.controller;

import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;

/**
 * 该接口为控制器接口，主要基本功能有
 * 1. 设置控制器关联的管理器线程池大小
 * 2. 设置 进出缓存区
 * 同时该接口继承了Runnable 支持线程操作
 */
public interface IController<T,E>{



    /**
     * 创建一块存放处理器处理完返回控制器结果的缓存区
     * @return
     */
     E createOutBuffer();


    /**
     * 初始化控制器
     */
    void init();

    /**
     * 处理一条请求操作
     * @return 表示当前传入的信息，剩下几条未执行，如果未0 表示正常
     */
    Integer execute(T t) throws ExecutionException, InterruptedException;

    /**
     * 销毁控制器
     */
    void destroy();

    /**
     * 判断当前控制器的管理器是否空闲
     * @return
     */
    Boolean isIdle();

}
