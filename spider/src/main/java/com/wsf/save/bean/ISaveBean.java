package com.wsf.save.bean;

import com.wsf.domain.Item;

import java.util.concurrent.ConcurrentLinkedQueue;

public interface ISaveBean {
    /**
     * saveBean启动方法,返回值为url队列，可以为空，如果有返回必须为地址，会放入到url池中
     */
    ConcurrentLinkedQueue<String> start(Item item) throws Exception;
}
