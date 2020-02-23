package com.wsf.controller.request;

import com.wsf.controller.IController;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


public interface IRequestController extends IController<ConcurrentLinkedQueue<String>, ConcurrentHashMap<String,byte[]>> {
    /**
     * 读入一批数据
     * @return
     */
    Integer executeBatch(LinkedList<ConcurrentLinkedQueue<String>> inBufferList);

}
