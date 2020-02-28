package com.wsf.controller.request;

import com.wsf.controller.IController;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


public interface IRequestController extends IController<ConcurrentLinkedQueue<String>, ConcurrentHashMap<String, byte[]>> {
//    Integer executeBatch(LinkedList<ConcurrentLinkedQueue> inBufferList);
    /**
     * 判断当前控制器的管理器是否有空闲线程
     *
     * @return
     */
    Boolean isIdle();

    /**
     * 当前运行队列是否为空
     *
     * @return
     */
    Boolean isEmpty();
}
