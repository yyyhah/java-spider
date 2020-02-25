package com.wsf.controller.center;

import com.wsf.controller.IController;


/**
 * 中央调度器，主要进行其他几个调度器执行的管理
 * 1. 分配资源，几个调度器的时间占比
 * 2. 负责其他几个调度器的关闭，初始化。
 */
public interface ICenterController extends IController {

    /**
     * 请求器执行一组数据
     */
    void startOneRequest();
//
//    /**
//     * 请求器执行多组数据
//     */
//    void startBatchRequest();


    @Override
    default void init() {

    }

    @Override
    default Integer execute(Object o) {
        return null;
    }

    @Override
    default void destroy() {

    }

    @Override
    default Boolean isIdle() {
        return null;
    }

    @Override
    default Boolean isEmpty() {
        return null;
    }

}
